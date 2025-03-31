package com.toongather.toongather.domain.member.api;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.JoinFormRequest;
import com.toongather.toongather.domain.member.dto.MemberRequest;
import com.toongather.toongather.domain.member.dto.MemberRequest.LoginRequest;
import com.toongather.toongather.domain.member.dto.MemberRequest.SearchMemberRequest;
import com.toongather.toongather.domain.member.dto.MemberResponse;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.member.service.AuthService;
import com.toongather.toongather.domain.member.service.EmailService;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.global.common.error.CommonError;
import com.toongather.toongather.global.common.error.CommonRuntimeException;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(MemberApi.class)
class MemberApiTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  MemberService memberService;

  @MockBean
  MemberRepository memberRepository;

  @MockBean
  JwtTokenProvider jwtTokenProvider;

  @MockBean
  EmailService emailService;

  @MockBean
  AuthService authService;

  private MemberRequest member;

  private Member memberEntity;

  private HttpHeaders httpHeaders;

  @BeforeEach
  void setUp() {

    memberEntity = Member.builder()
        .memberId(1L)
        .name("test")
        .email("test@gmail.com")
        .phone("010-1234-5678")
        .password("1234")
        .nickName("test")
        .build();
    memberEntity.updateActiveMember();

    member = new MemberRequest(memberEntity);


    httpHeaders = new HttpHeaders();
    httpHeaders.add("Authorization", "Bearer " + "token");
    httpHeaders.add("X-RT_TOKEN", "Bearer " + "refreshToken");

  }

  @Test
  @DisplayName("회원가입 시 정상적으로 등록된다")
  void 회원가입() throws Exception {
    //given
    JoinFormRequest joinForm = new JoinFormRequest();
    joinForm.setName("test");
    joinForm.setPhone("010-1234-5678");
    joinForm.setEmail("younie.jang@gmail.com");
    joinForm.setNickName("test");
    joinForm.setPassword("1234");

    given(memberService.join(ArgumentMatchers.any(JoinFormRequest.class))).willReturn(memberEntity);

    //when
    //then
    mockMvc.perform(post("/member/join")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(joinForm)).with(csrf()))
        .andExpect(status().isOk())
        .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
  }

  @Test
  @DisplayName("로그인 시 정상적으로 로그인 된다")
  void 로그인_성공() throws Exception {
    //given
    LoginRequest request = new LoginRequest();
    request.setEmail("email@gmail.com");
    request.setPassword("password");

    //when
    given(memberService.loginMember(request.getEmail(), request.getPassword()))
        .willReturn(member);
    given(authService.setAccessTokenHeader(member)).willReturn(httpHeaders);

    //then
    mockMvc.perform(post("/member/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)).with(csrf()))
        .andExpect(status().isOk())
        .andExpect(header().exists("Authorization"))
        .andDo(result -> System.out.println(result.getResponse().getContentAsString()));

  }

  @Test
  @DisplayName("잘못된 비밀번호로 인해 로그인이 실패한다")
  void 로그인_실패() throws Exception {
    //given
    LoginRequest request = new LoginRequest();
    request.setEmail("email@gmail.com");
    request.setPassword("incorrect password");

    //when
    given(memberService.loginMember(request.getEmail(), request.getPassword()))
        .willThrow(new CommonRuntimeException(CommonError.USER_NOT_PASSWORD));

    //then
    mockMvc.perform(post("/member/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isUnauthorized())
        .andDo(result -> System.out.println(result.getResponse()
            .getContentAsString()));
  }

  @Test
  @DisplayName("로그인 시 이메일을 잘못 적어 로그인에 실패한다")
  void 로그인_검증_실패() throws Exception {
    //given
    LoginRequest request = new LoginRequest();
    request.setEmail("test");
    request.setPassword("test");

    //when
    given(memberService.loginMember(request.getEmail(), request.getPassword()))
        .willReturn(member);

    //then
    mockMvc.perform(post("/member/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isBadRequest())
        .andDo(result -> System.out.println(result.getResponse().getContentAsString()));

  }

  @Test
  @DisplayName("임시회원인 경우 코드를 입력하여 임시회원 인증 서비스가 실행된다")
  void 임시회원_인증() throws Exception {
    //given
    member.setTempCode("tempCode");

    //then
    mockMvc.perform(post("/member/confirm")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(member))
            .with(csrf()))
        .andExpect(status().isOk());

    verify(memberService).confirmMemberByTempCode(member.getId(), member.getTempCode());

  }

  @Test
  @DisplayName("비밀번호와 이메일을 통해 회원이 존재하면 회원 정보를 응답값에 넘겨준다")
  void 회원_찾기() throws Exception{
    //given
    SearchMemberRequest request = new SearchMemberRequest();
    request.setName("test");
    request.setPhone("010-1234-5678");

    //when
    ConcurrentMap<String, Object> result =
        new ConcurrentHashMap<>(Map.of("email", "test@gmail.com", "id", 1L));

    given(memberService.findMemberByNameAndPhone(request.getName(), request.getPhone()))
        .willReturn(result);

    //then
    mockMvc.perform(get("/member/search/members")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request))
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.email").value(result.get("email")))
        .andExpect(jsonPath("$.data.id").value(result.get("id")));

  }

  @DisplayName("회원 정보를 성공적으로 가져온다")
  @Test
  void 회원_정보_가져오기() throws Exception {
    //given
    Long id = 1L;
    MemberResponse result = MemberResponse.from(memberEntity);

    //when
    given(memberService.findMemberById(id)).willReturn(result);

    //then
    mockMvc.perform(get("/member/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.id").value(result.getId()))
        .andExpect(jsonPath("$.data.email").value(result.getEmail()))
        .andExpect(jsonPath("$.data.name").value(result.getName()))
        .andExpect(jsonPath("$.data.phone").value(result.getPhone()))
        .andExpect(jsonPath("$.data.nickname").value(result.getNickname()));

  }



}
