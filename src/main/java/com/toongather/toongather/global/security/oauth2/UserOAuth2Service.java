package com.toongather.toongather.global.security.oauth2;


import com.toongather.toongather.domain.member.domain.JoinType;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.domain.Role;
import com.toongather.toongather.domain.member.domain.RoleType;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.member.repository.MemberRoleRepository;
import com.toongather.toongather.domain.member.repository.RoleRepository;
import com.toongather.toongather.global.security.oauth2.OAuth2Provider.Google;
import com.toongather.toongather.global.security.oauth2.OAuth2Provider.Kakao;
import com.toongather.toongather.global.security.oauth2.OAuth2Provider.Naver;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserOAuth2Service extends DefaultOAuth2UserService {

  private final MemberRepository memberRepository;
  private final RoleRepository roleRepository;
  private final MemberRoleRepository memberRoleRepository;

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();

    //이미 존재하는 email인 경우 로그인, 아닌경우 회원가입 로직
    Member member = makeUserByOAuthUser(oAuth2User.getAttributes(), registrationId);
    Member findMember = memberRepository.findByEmail(member.getEmail()).orElse(null);
    if (findMember == null) {
      Role role = roleRepository.findByName(RoleType.ROLE_USER);
      member.addMemberRoles(role);
      memberRepository.save(member);
    }
    return new DefaultOAuth2User(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
        oAuth2User.getAttributes(),
        userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
            .getUserNameAttributeName()
    );
  }

  public Member makeUserByOAuthUser(Map<String, Object> attributes, String snsType) {

    return switch (snsType) {
      case Google.PROVIDER_NAME -> ofGoogle(attributes);
      case Naver.PROVIDER_NAME -> ofNaver((Map<String, Object>) attributes.get(Naver.WRAPPER));
      case Kakao.PROVIDER_NAME -> ofKakao((Map<String, Object>) attributes.get(Kakao.WRAPPER));
      default -> null;
    };
  }

  private Member ofGoogle(Map<String, Object> attributes) {
    return Member.OAuthBuilder()
        .name(String.valueOf(attributes.get(Google.SCOPE_NAME)))
        .email(String.valueOf(attributes.get(Google.SCOPE_EMAIL)))
        .nickName(String.valueOf(attributes.get(Google.SCOPE_NICKNAME)))
        .joinType(JoinType.GOOGLE)
        .build();
  }

  private Member ofNaver(Map<String, Object> attributes) {
    return Member.OAuthBuilder()
        .name(String.valueOf(attributes.get(Naver.SCOPE_NAME)))
        .email(String.valueOf(attributes.get(Naver.SCOPE_EMAIL)))
        .nickName(String.valueOf(attributes.get(Naver.SCOPE_NICKNAME)))
        .joinType(JoinType.NAVER)
        .build();
  }

  private Member ofKakao(Map<String, Object> attributes) {
    Map<String, Object> properties = (Map<String, Object>) attributes.get(Kakao.PROFILE);

    return Member.OAuthBuilder()
        .name(String.valueOf(properties.get(Kakao.SCOPE_NAME)))
        .email(String.valueOf(attributes.get(Kakao.SCOPE_EMAIL)))
        .nickName(String.valueOf(properties.get(Kakao.SCOPE_NICKNAME)))
        .joinType(JoinType.KAKAO)
        .build();
  }


}
