package com.toongather.toongather.domain.member.service;

import static org.junit.jupiter.api.Assertions.*;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.JoinFormDTO;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

  @Autowired MemberService memberService;
  @Autowired
  MemberRepository memberRepository;

  @Test
  @DisplayName("회원가입 서비스 테스트")
//  @Rollback(false)
  void join() {

    //given
    JoinFormDTO joinFormDTO = new JoinFormDTO();
    joinFormDTO.setEmail("ddddd@naver.com");
    joinFormDTO.setName("테스트");
    joinFormDTO.setNickName("테스트닉네임");
    joinFormDTO.setPassword("1234");
    joinFormDTO.setPhone("010-7666-1111");

    //when
    Long id = memberService.join(joinFormDTO);

    Member member =  memberRepository.findOne(id);

    //then
    Assertions.assertThat(member.getName()).isEqualTo("테스트");

  }

  @Test
  @DisplayName("로그인 시 refesh token 및 최신 날짜 update")
  void updateTokenAndLoginHistoryById() {
    //given
    Long memberId = 37l;
    String refreshToken = "refresh token test";

    //when
    memberService.updateTokenAndLoginHistoryById(memberId, refreshToken);
    Member one = memberRepository.findOne(memberId);

    //then
    Assertions.assertThat(one.getRefreshToken()).isEqualTo(refreshToken);
    System.out.println("member login history = " + one.getLastLogin());

  }


}