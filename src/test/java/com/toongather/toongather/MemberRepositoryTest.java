package com.toongather.toongather;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() {
        Member member = new Member("memberA", "1234");

        Long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);
        Assertions.assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장
    }
}
