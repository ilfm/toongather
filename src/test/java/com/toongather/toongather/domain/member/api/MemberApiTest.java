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
import com.toongather.toongather.domain.member.domain.MemberType;
import com.toongather.toongather.domain.member.dto.JoinFormDTO;
import com.toongather.toongather.domain.member.dto.MemberDTO;
import com.toongather.toongather.domain.member.dto.MemberDTO.LoginRequest;
import com.toongather.toongather.domain.member.dto.MemberDTO.SearchMemberRequest;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.member.service.AuthService;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.global.security.jwt.JwtTokenProvider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.BeforeEach;
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
  AuthService authService;

  private MemberDTO member;

  private HttpHeaders httpHeaders;

  @BeforeEach
  void setUp() {

    Member memberEntity = Member.builder()
        .name("test")
        .email("test@gmail.com")
        .phone("010-1234-5678")
        .password("1234")
        .nickName("test")
        .build();

    memberEntity.setId(1L);
    memberEntity.setMemberType(MemberType.ACTIVE);

    member = new MemberDTO(memberEntity);

    httpHeaders = new HttpHeaders();
    httpHeaders.add("Authorization", "Bearer " + "token");
    httpHeaders.add("X-RT_TOKEN", "Bearer " + "refreshToken");

  }

  @Test
  void 회원가입() throws Exception {
    //given
    JoinFormDTO joinFormDTO = new JoinFormDTO();
    joinFormDTO.setName("test");
    joinFormDTO.setPhone("010-1234-5678");
    joinFormDTO.setEmail("younie.jang@gmail.com");
    joinFormDTO.setNickName("test");
    joinFormDTO.setPassword("1234");

    given(memberService.join(ArgumentMatchers.any(JoinFormDTO.class))).willReturn(1L);

    //when
    //then
    mockMvc.perform(post("/member/join")
        .contentType(MediaType.APPLICATION_JSON)
        .content(new ObjectMapper().writeValueAsString(joinFormDTO)).with(csrf()))
        .andExpect(status().isOk())
        .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
  }
  @Test
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
  void 로그인_실패() throws Exception {
    //given
    LoginRequest request = new LoginRequest();
    request.setEmail("email@gmail.com");
    request.setPassword("password");

    //when
    given(memberService.loginMember(request.getEmail(), request.getPassword()))
        .willReturn(null);

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





}
