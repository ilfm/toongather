package com.toongather.toongather.domain.member.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class JoinFormDTO {

  @NotEmpty
  private String name;

  @NotEmpty
  private String email;

  private String phone;

  @NotEmpty
  private String nickName;

  @NotEmpty
  private String password;


}
