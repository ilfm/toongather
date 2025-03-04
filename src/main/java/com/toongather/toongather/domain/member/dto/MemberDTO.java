package com.toongather.toongather.domain.member.dto;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.domain.MemberType;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDTO {

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


  public MemberDTO(Member member) {
    this.id = member.getId();
    this.email = member.getEmail();
    this.name = member.getName();
    this.nickname = member.getNickName();
    this.phone = member.getPhone();
    this.refreshToken = member.getRefreshToken();
    this.roleNames = member.getMemberRoles()
        .stream().map(target -> target.getRole().getName().name())
        .collect(Collectors.toList());
    this.memberType = member.getMemberType();
  }

  @Getter
  @Setter
  public static class LoginRequest {
    @NotNull
    @Email
    private String email;
    @NotEmpty
    private String password;
  }

  @Getter
  @Setter
  public static class TempCodeRequest {
    @NotNull
    Long id;

    @NotNull
    private String tempCode;
  }

  @Getter
  @Setter
  public static class SearchMemberRequest {
    @NotNull
    private String name;
    @NotNull
    private String phone;
  }

}
