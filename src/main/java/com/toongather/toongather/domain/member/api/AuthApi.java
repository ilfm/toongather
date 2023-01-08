package com.toongather.toongather.domain.member.api;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.SnsResponseDTO;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.global.security.jwt.JwtToken;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthApi {

  //임시
  private final String GOOGLE_CLIENT_ID = "999850001845-d6crqr0hmnot2221173uk00p5hn2nd34.apps.googleusercontent.com";

  private final MemberRepository repository;
  private final JwtTokenProvider jwtTokenProvider;


  @GetMapping("/test")
  public String test() {
    return "hello";
  }


  @PostMapping("/sns")
  @Transactional
  public ResponseEntity<Object> getAuthCode(@RequestBody SnsResponseDTO dto) throws GeneralSecurityException, IOException {

    log.info(dto.toString());

    //구글과 서버 통신을 하기위해서 사용하는듯함.
    NetHttpTransport netHttpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

    GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(netHttpTransport, jsonFactory)
      .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
      .build();

    //토큰 검증
    GoogleIdToken idToken = verifier.verify(dto.getCredential());

    if(idToken != null) {
      GoogleIdToken.Payload payload = idToken.getPayload();

      //토큰값을 통해 정보 읽어오기
      String userId = payload.getSubject();
      String email = payload.getEmail();
      boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
      String name = (String) payload.get("name");

      //가입이력이 없을때는 1) 회원가입 페이지에 데이터와 함께 가입 필요 코드 넘겨주기
      Member member = repository.getJoinedMember(email);
      if(member == null) {
        ConcurrentHashMap<Object, Object> result = new ConcurrentHashMap<>();
        result.put("code", "JOIN_NEED");
        result.put("email", email);
        result.put("name", name);
        return new ResponseEntity<>(result, HttpStatus.OK);
      }

      //가입이력이 있을 경우 jwt 토큰 발급
      List<String> memberRoles = member.getMemberRoles().stream()
        .map(target -> target.getRole().getName())
        .collect(Collectors.toList());
      String token = jwtTokenProvider.createToken(member.getId(), memberRoles);

      //note refresh token 1) 로그인시 생성
      String refreshToken = jwtTokenProvider.createRefreshToken(member.getId());

      //note refresh token 2) token을 db에 저장
      member.setRefreshToken(refreshToken);
      repository.saveRefreshToken(member);

      HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add("AT_TOKEN", "Bearer " + token);
      httpHeaders.add("RT_TOKEN", "Bearer " + refreshToken);

      JwtToken jwtTokens = JwtToken.builder().accessToken(token).refreshToken(refreshToken).build();


      return new ResponseEntity<>(jwtTokens, httpHeaders, HttpStatus.OK);


    } else {
      return new ResponseEntity<>("invalid id token", HttpStatus.BAD_REQUEST);
    }

  }



}
