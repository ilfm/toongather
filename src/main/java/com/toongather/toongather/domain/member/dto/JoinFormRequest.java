package com.toongather.toongather.domain.member.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
public class JoinFormRequest {

  @NotNull
  private String name;

  @NotNull
  private String email;

  private String phone;

  @NotNull
  private String nickName;

  @NotNull
  private String password;


}
