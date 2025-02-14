package com.toongather.toongather.global.common.advice;

import com.toongather.toongather.domain.member.api.MemberApi;
import com.toongather.toongather.global.common.ApiResponse;
import com.toongather.toongather.global.common.error.CommonErrorInfo;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice(assignableTypes = {MemberApi.class})
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

  //특정 응답에 대해 공통 처리를 할지 말지 결정하는 메서드 -> 특정 컨트롤러의 응답 혹은 특정 메시지 컨버터가 사용될때만 허용
  //String 타입만 String Http Message Converter를 사용하고 있기때문에 그 부분만 제외해야 에러가 나지 않는다.
  @Override
  public boolean supports(MethodParameter returnType,
      Class<? extends HttpMessageConverter<?>> converterType) {

    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    //로그인 요청에서는 직접 토큰 헤더를 설정해주어야 하므로 공통 응답을 타지 않도록 처리
    if(requestAttributes != null) {
      HttpServletRequest request = requestAttributes.getRequest();
      if(request.getRequestURI().equals("/member/login")) {
        return false;
      }
    }

    return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
  }

  //body에 담길내용
  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType,
      MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
      ServerHttpRequest request, ServerHttpResponse response) {

   String path = request.getURI().getPath();

   //에러 처리된 객체가 넘어올 때
   if(body instanceof CommonErrorInfo) {
     return false;
   }

   return ApiResponse.builder()
       .path(path)
       .data(body)
       .message("success")
       .build();
  }
}
