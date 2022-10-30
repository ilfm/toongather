package com.toongather.toongather.domain.member.api;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.repository.MemberRepository;
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

    @PostMapping("/login")
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

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);


        return new ResponseEntity<>(token, httpHeaders, HttpStatus.OK);

    }

    @GetMapping("/test")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String test() {
        return "success";
    }

}
