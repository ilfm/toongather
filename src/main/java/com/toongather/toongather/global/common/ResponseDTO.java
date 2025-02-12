package com.toongather.toongather.global.common;

import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResponseDTO {

  private String code;
  private String message;
  private Map<String, Object> data;

}
