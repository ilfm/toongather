package com.toongather.toongather.domain.member.dto;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.domain.MemberType;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequest {

  private Long id;
  private String email;
  private String name;
  private String nickname;
  private String phone;
  private String password;
  private String refreshToken;
  private List<String> roleNames;
  private String tempCode;
  private MemberType memberType;

  @Builder
  public MemberRequest(Long id, String email, String name, String nickname, List<String> roleNames,
      MemberType memberType) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.nickname = nickname;
    this.roleNames = roleNames;
    this.memberType = memberType;
  }

  public MemberRequest(Member member) {
    this.id = member.getId();
    this.email = member.getEmail();
    this.name = member.getName();
    this.nickname = member.getNickName();
    this.phone = member.getPhone();
    this.refreshToken = member.getRefreshToken();
    this.roleNames = member.getMemberRoles()
        .stream()
        .map(target -> target.getRole()
            .getName()
            .name())
        .collect(Collectors.toList());
    this.memberType = member.getMemberType();
  }

  @Getter
  @Setter
  public static class LoginRequest {

    @NotBlank
    @Email
    private String email;
    @NotEmpty
    private String password;
  }

  @Getter
  @Setter
  public static class TempCodeRequest {

    @NotBlank
    Long id;

    @NotNull
    private String tempCode;
  }

  @Getter
  @Setter
  public static class SearchMemberRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String phone;
  }

}
