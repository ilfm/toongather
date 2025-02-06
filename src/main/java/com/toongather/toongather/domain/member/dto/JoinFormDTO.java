package com.toongather.toongather.domain.member.dto;

import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@NoArgsConstructor
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
