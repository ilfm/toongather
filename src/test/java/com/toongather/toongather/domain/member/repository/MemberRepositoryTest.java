package com.toongather.toongather.domain.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.toongather.toongather.domain.member.domain.JoinType;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.domain.MemberType;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class MemberRepositoryTest {

  @Autowired
  private MemberRepository memberRepository;

  @Test
  @DisplayName("email을 통해 멤버를 찾는다")
  void findByEmail() {

    Member member = Member.builder()
        .email("test@gmail.com")
        .name("test")
        .nickName("testnickname")
        .phone("010-123-1234")
        .password("1234")
        .build();

    memberRepository.save(member);

    Optional<Member> result = memberRepository.findByEmail(member.getEmail());

    assertThat(result).isNotNull();
    assertThat(result.get().getEmail()).isEqualTo(member.getEmail());
    assertThat(result.get().getName()).isEqualTo(member.getName());
    assertThat(result.get().getJoinType()).isEqualTo(JoinType.NORMAL);
    assertThat(result.get().getMemberType()).isEqualTo(MemberType.TEMP);

  }

}
