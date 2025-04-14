package com.toongather.toongather.domain.member.dto;

import com.toongather.toongather.domain.member.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

  private Long id;
  private String email;
  private String name;
  private String phone;
  private String nickname;

  @Builder
  public MemberResponse(Long id, String email, String name, String phone, String nickname) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.phone = phone;
    this.nickname = nickname;
  }

  public static MemberResponse from(Member member) {
    return MemberResponse.builder()
        .id(member.getId())
        .email(member.getEmail())
        .name(member.getName())
        .phone(member.getPhone())
        .nickname(member.getNickName())
        .build();
  }

}
