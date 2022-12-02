package com.toongather.toongather.domain.member.api;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.domain.Role;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.member.repository.RoleRepository;
import com.toongather.toongather.global.security.jwt.JwtToken;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MemberController {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @PostMapping("/join")
    @Transactional
    public Long join() {
        log.info("회원가입");

        Member member = Member.builder()
                .name("memberB")
                .password(passwordEncoder.encode("1234"))
                .build();

        Long id = memberRepository.save(member);
        return id;

    }

    @PostMapping("/createAdmin")
    @Transactional
    public Long createAdmin() {
        log.info("관리자 등록");
        Role adminRole = Role.builder().name("관리자").build();
        roleRepository.saveAdmin(adminRole);
        return 1l;
    }

    @PostMapping("/refresh")
    @Transactional
    public ResponseEntity refreshToken(@RequestBody Map<String, String> user, HttpServletRequest request){

        JwtToken tokens = jwtTokenProvider.resolveToken(request);

        //note 1. access token이 만료됨에따라, refresh token을 확인하여 검증
        // - refresh token 유효한 경우 ->
        //  1) refresh token db 조회하여 일치 하는지 확인
        //  2) 일치한다면 access token 재발급, 일치하지 않는다면 에러
        // - refresh token 만료된 경우
        //  1) 에러발생, 재로그인 요청

        switch (jwtTokenProvider.validateToken(tokens.getRefreshToken())) {
            case DENIED:
            case EXPIRED :
                return new ResponseEntity("login need", HttpStatus.UNAUTHORIZED);
            case ACCESS :
                Long memberId = Long.valueOf(user.get("id"));
                Member member = Optional.ofNullable(memberRepository.find(Long.valueOf(memberId)))
                  .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
                List<String> memberRoles = member.getMemberRoles().stream()
                  .map(target -> target.getRole().getName())
                  .collect(Collectors.toList());

                if(member.getRefreshToken().equals(tokens.getRefreshToken())) {
                    String token = jwtTokenProvider.createToken(member.getId(), memberRoles);
                    String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());
                    member.setRefreshToken(refreshToken);
                    memberRepository.saveRefreshToken(member);
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.add("AT_TOKEN", "Bearer " + token);
                    httpHeaders.add("RT_TOKEN", "Bearer " + refreshToken);

                    JwtToken jwtTokens = JwtToken.builder().accessToken(token).refreshToken(refreshToken).build();
                    return new ResponseEntity<>(jwtTokens, httpHeaders, HttpStatus.OK);
                }
                break;
        }

        return new ResponseEntity("login need", HttpStatus.UNAUTHORIZED);
    }



    @PostMapping("/login")
    @Transactional
    public ResponseEntity login(@RequestBody Map<String, String> user) {
        log.info("login");
        Long memberId = Long.valueOf(user.get("id"));
        Member member = Optional.ofNullable(memberRepository.find(Long.valueOf(memberId)))
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));


        List<String> memberRoles = member.getMemberRoles().stream()
                .map(target -> target.getRole().getName())
                .collect(Collectors.toList());
        //권한과 id값을 통해 jwt 생성
        String token = jwtTokenProvider.createToken(member.getId(), memberRoles);

        //note refresh token 1) 로그인시 생성
        String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

        //note refresh token 2) token을 db에 저장
        member.setRefreshToken(refreshToken);
        memberRepository.saveRefreshToken(member);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("AT_TOKEN", "Bearer " + token);
        httpHeaders.add("RT_TOKEN", "Bearer " + refreshToken);

        JwtToken jwtTokens = JwtToken.builder().accessToken(token).refreshToken(refreshToken).build();


        return new ResponseEntity<>(jwtTokens, httpHeaders, HttpStatus.OK);

    }

    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String test() {
        return "success";
    }

}
