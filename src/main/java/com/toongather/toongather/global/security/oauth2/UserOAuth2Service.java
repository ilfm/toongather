package com.toongather.toongather.global.security.oauth2;


import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.repository.MemberRepository;
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

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration().getRegistrationId();

    //구글, 네이버, 카카오에 따라 attribute값 맞게 세팅하여 member 객체 생성

    //이미 존재하는 email인 경우 로그인, 아닌경우 회원가입 로직
    log.info("oAuth2User = {}" , oAuth2User);
    Member member = makeUserByOAuthUser(oAuth2User.getAttributes(), registrationId);

    if(memberRepository.getJoinedMember(member.getEmail()) == null) {
      log.info("회원가입");
    }

    return new DefaultOAuth2User(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")),
      oAuth2User.getAttributes(),
      userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName()
      );
  }

  public Member makeUserByOAuthUser(Map<String, Object> attributes, String snsType) {
      Member member = null;
    switch(snsType) {
      case "google" :
         member = ofGoogle(attributes);
         break;
      case "naver" :
        member = ofNaver((Map<String, Object>) attributes.get("response"));
        break;
      case "kakao" :
        member = ofKakao((Map<String, Object>) attributes.get("kakao_account"));
        break;
    }

    return member;
  }

  private Member ofGoogle(Map<String, Object> attributes) {
    return Member.OAuthBuilder()
      .name(String.valueOf(attributes.get("name")))
      .email(String.valueOf(attributes.get("email")))
      .nickName(String.valueOf(attributes.get("given_name")))
      .build();
  }

  private Member ofNaver(Map<String, Object> attributes) {
    return Member.OAuthBuilder()
      .name(String.valueOf(attributes.get("name")))
      .email(String.valueOf(attributes.get("email")))
      .nickName(String.valueOf(attributes.get("nickname")))
      .build();
  }

  private Member ofKakao(Map<String, Object> attributes) {
    Map<String, Object> properties = (Map<String, Object>) attributes.get("profile");

    return Member.OAuthBuilder()
      .name(String.valueOf(properties.get("nickname")))
      .email(String.valueOf(attributes.get("email")))
      .nickName(String.valueOf(properties.get("nickname")))
      .build();
  }

  //구글
  //name, given_name, email

  //카카오
  //kakao_account { profile : nickname , email }


  //naver
  //response -> nickname, email, name


}