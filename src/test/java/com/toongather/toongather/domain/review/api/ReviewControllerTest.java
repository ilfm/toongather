package com.toongather.toongather.domain.review.api;

import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toongather.toongather.domain.review.dto.CreateReviewRequest;
import com.toongather.toongather.domain.review.dto.UpdateReviewRequest;
import com.toongather.toongather.domain.review.service.ReviewService;
import com.toongather.toongather.global.common.error.custom.ReviewException.ReviewNotFoundException;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.mockito.Mockito.verify;

import com.toongather.toongather.global.common.advice.ApiExceptionAdvice;


@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ReviewService reviewService;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(new ReviewController(reviewService))
        .addFilters(new CharacterEncodingFilter("UTF-8", true))
        .setMessageConverters(new MappingJackson2HttpMessageConverter())
        .alwaysDo(print())
        .setControllerAdvice(new ApiExceptionAdvice()) // 예외 처리기 추가
        .build();
  }

  @DisplayName("리뷰 등록 성공")
  @Test
  void createReview() throws Exception {
    // given
    CreateReviewRequest request = CreateReviewRequest.builder()
        .memberId(1L)
        .toonId(1L)
        .recommendComment("테스트 리뷰")
        .star(5L)
        .build();

    ArgumentCaptor<CreateReviewRequest> captor = ArgumentCaptor.forClass(CreateReviewRequest.class);

    // when & then
    mockMvc.perform(post("/reviews/new")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());

    verify(reviewService, times(1)).createReview(captor.capture());
    CreateReviewRequest captorValue = captor.getValue();

    assertThat(captorValue.getRecommendComment()).isEqualTo("테스트 리뷰");
    assertThat(captorValue.getStar()).isEqualTo(5L);
    assertThat(captorValue.getToonId()).isEqualTo(1L);
    assertThat(captorValue.getMemberId()).isEqualTo(1L);
  }

  @DisplayName("리뷰 수정 성공")
  @Test
  public void updateReview_success() throws Exception {
    // given
    Long reviewId = 1L;
    UpdateReviewRequest request = UpdateReviewRequest.builder()
        .reviewId(reviewId)
        .recommendComment("수정된 리뷰")
        .star(5L)
        .build();

    ArgumentCaptor<UpdateReviewRequest> captor = ArgumentCaptor.forClass(UpdateReviewRequest.class);
    willDoNothing().given(reviewService).updateReview(request);

    // when & Then
    mockMvc.perform(put("/reviews/{reviewId}", reviewId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
    verify(reviewService, times(1)).updateReview(captor.capture());

    UpdateReviewRequest captured = captor.getValue();
    assertThat(captured.getReviewId()).isEqualTo(reviewId);
    assertThat(captured.getRecommendComment()).isEqualTo("수정된 리뷰");
    assertThat(captured.getStar()).isEqualTo(5L);
  }

  @DisplayName("리뷰 수정 실패")
  @Test
  public void updateReview_NotFound() throws Exception {
    // given
    Long reviewId = -1L;
    UpdateReviewRequest request = UpdateReviewRequest.builder()
        .reviewId(reviewId)
        .recommendComment("수정 실패 리뷰")
        .star(4L)
        .build();

    BDDMockito.willThrow(new ReviewNotFoundException()).given(reviewService)
        .updateReview(any(UpdateReviewRequest.class));

    // when & then
    mockMvc.perform(put("/reviews/{reviewId}", reviewId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("해당 리뷰가 존재하지 않습니다."));

    verify(reviewService, times(1)).updateReview(any(UpdateReviewRequest.class));
  }

  @DisplayName("리뷰 삭제 성공")
  @Test
  public void deleteReview_success() throws Exception {
    // given
    Long reviewId = 1L;
    willDoNothing().given(reviewService).deleteReview(reviewId);

    // when
    mockMvc.perform(delete("/reviews/{reviewId}", reviewId))
        .andExpect(status().isOk());

    // then
    verify(reviewService, times(1)).deleteReview(reviewId);
  }

  @DisplayName("리뷰 삭제 실패")
  @Test
  public void deleteReview_NotFound() throws Exception {
    // given
    Long reviewId = 1L;
    willThrow(new ReviewNotFoundException())
        .given(reviewService).deleteReview(reviewId);

    // when
    mockMvc.perform(delete("/reviews/{reviewId}", reviewId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("해당 리뷰가 존재하지 않습니다."));

    // then
    verify(reviewService, times(1)).deleteReview(reviewId);
  }
}