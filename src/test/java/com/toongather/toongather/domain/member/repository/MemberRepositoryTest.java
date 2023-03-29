package com.toongather.toongather.domain.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.toongather.toongather.domain.member.domain.Member;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("회원 저장")
    public void saveMember() {
        Member member = Member.builder()
          .phone("010-0000-0000")
          .nickName("test1")
          .email("0000@naver.com")
          .password("2345")
          .build();

        memberRepository.save(member);
        Member findMember = memberRepository.findOne(member.getId());

        assertThat(findMember).isEqualTo(member);
    }

    @Test
    @DisplayName("비밀번호 일치 확인")
    public void checkPwd() {
        Member member = memberRepository.findOne(37l);
        String password = "2345";
        assertThat(passwordEncoder.matches(password, member.getPassword())).isTrue();
    }


    @Test
    @DisplayName("권한 리스트들을 가져오기")
    public void findRoles() {
        Member member = memberRepository.findOne(37l);
        List<String> memberRoles = member.getMemberRoles().stream()
            .map(target -> target.getRole().getName().name())
            .collect(Collectors.toList());
        for (String memberRole : memberRoles) {
            System.out.println("memberRole = " + memberRole);
        }

    }


}
