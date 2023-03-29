package com.toongather.toongather.global.config;

import com.toongather.toongather.global.security.jwt.JwtAccessDeniedHandler;
import com.toongather.toongather.global.security.jwt.JwtAuthenticationEntryPoint;
import com.toongather.toongather.global.security.jwt.JwtAuthenticationFilter;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import com.toongather.toongather.global.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.toongather.toongather.global.security.oauth2.OAuth2AuthenticationFailureHandler;
import com.toongather.toongather.global.security.oauth2.OAuth2AuthenticationSuccessHandler;
import com.toongather.toongather.global.security.oauth2.UserOAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity // 기본적인 웹보안 활성화
@EnableGlobalMethodSecurity(prePostEnabled = true) //@preAuthorize 어노테이션 메소드단위로 추가하기위함
public class SecurityConfig {

  private final JwtTokenProvider jwtTokenProvider;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final CorsFilter corsFilter;

  private final UserOAuth2Service userOAuth2Service;

  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

  private final OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

  private final HttpCookieOAuth2AuthorizationRequestRepository authorizationRequestRepository;


  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  public WebSecurityCustomizer configure() {

    //url 제외 패턴
    return (web) -> web.ignoring().mvcMatchers(
      "/api/v1/ignore"
    );
  }

  @Bean
  public BCryptPasswordEncoder encodePwd() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http
      .cors()
        .and()
      .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
      .csrf()
        .disable()
      .formLogin()
        .disable()
      .httpBasic()
        .disable()
      .exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
        .accessDeniedHandler(jwtAccessDeniedHandler)
        .and()
      .authorizeRequests()
        .antMatchers("/**", "/member/join", "/member/login", "/oauth2/**")
          .permitAll()
        .antMatchers("/member/user")
          .authenticated()
        .and()
      .oauth2Login()
        .authorizationEndpoint()
          .baseUri("/oauth2/authorize")
          .authorizationRequestRepository(authorizationRequestRepository)
          .and()
        .redirectionEndpoint()
          .baseUri("/oauth2/callback/*")
          .and()
        .userInfoEndpoint()
          .userService(userOAuth2Service)
          .and()
        .successHandler(oAuth2AuthenticationSuccessHandler)
        .failureHandler(oAuth2AuthenticationFailureHandler);

      http.addFilter(corsFilter)
      .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);


      return http.build();

  }




}
