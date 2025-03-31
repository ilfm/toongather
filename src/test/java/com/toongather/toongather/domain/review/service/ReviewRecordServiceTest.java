package com.toongather.toongather.domain.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.toongather.toongather.domain.member.dto.JoinFormRequest;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewRecord;
import com.toongather.toongather.domain.review.dto.CreateReviewRecordResponse;
import com.toongather.toongather.domain.review.dto.ReviewRecordRequest;
import com.toongather.toongather.domain.review.dto.ReviewRecordResponse;
import com.toongather.toongather.domain.review.repository.ReviewRecordRepository;
import com.toongather.toongather.domain.webtoon.dto.WebtoonCreateResponse;
import com.toongather.toongather.domain.webtoon.dto.WebtoonRequest;
import com.toongather.toongather.domain.webtoon.service.WebtoonService;
import com.toongather.toongather.global.common.error.custom.ReviewException;
import groovy.util.logging.Slf4j;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;

import static org.mockito.BDDMockito.given;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ReviewRecordServiceTest {

  @Mock
  private WebtoonService webtoonService;
  @Mock
  private MemberService memberService;
  @InjectMocks
  private ReviewRecordService reviewRecordService;
  @Mock
  private ReviewService reviewService;
  @Mock
  private ReviewRecordRepository reviewRecordRepository;

  @DisplayName("리뷰 있는 경우 리뷰 기록 등록 (파일없음 추후 추가)")
  @Rollback
  @Test
  public void createReviewRecord() {
    //Given
    given(memberService.join(any(JoinFormRequest.class))).willReturn(1L);
    given(webtoonService.createWebtoon(any(WebtoonRequest.class)))
        .willReturn(WebtoonCreateResponse.builder().id(1L).build());

    Review existReview = Review.builder().reviewId(1L).recommendComment("리뷰 코멘트").build();
    given(reviewService.findById(existReview.getReviewId())).willReturn(existReview);

    Long memberId = memberService.join(new JoinFormRequest());
    WebtoonCreateResponse webtoonCreateResponse = webtoonService.createWebtoon(
        WebtoonRequest.builder().build());

    ReviewRecordRequest createReviewRecordRequest = ReviewRecordRequest.builder()
        .reviewId(existReview.getReviewId())
        .memberId(memberId)
        .record("리뷰 있는 기록 저장 테스트")
        .toonId(webtoonCreateResponse.getId())
        .build();

    //When
    CreateReviewRecordResponse createReviewRecordResponse = reviewRecordService.createReviewRecord(
        createReviewRecordRequest);

    //Then
    assertThat(createReviewRecordResponse).isNotNull();
    assertThat(createReviewRecordResponse.getRecord()).isEqualTo("리뷰 있는 기록 저장 테스트");
    assertThat(createReviewRecordResponse.getReviewId())
        .isEqualTo(existReview.getReviewId());

    verify(reviewService).findById(createReviewRecordResponse.getReviewId());
    verify(reviewService, never()).createDefaultReview(createReviewRecordRequest.getMemberId(),
        createReviewRecordRequest.getToonId());
  }

  @DisplayName("리뷰 없는 경우 리뷰 기록 등록 (파일없음 추후 추가)")
  @Rollback
  @Test
  public void createReviewRecordNoReview() {
    //Given
    given(memberService.join(any(JoinFormRequest.class))).willReturn(1L);
    given(webtoonService.createWebtoon(any(WebtoonRequest.class)))
        .willReturn(WebtoonCreateResponse.builder().id(1L).build());
    given(reviewService.createDefaultReview(any(Long.class), any(Long.class)))
        .willReturn(Review.builder().reviewId(1L).build());

    Long memberId = memberService.join(new JoinFormRequest());
    WebtoonCreateResponse webtoonCreateResponse = webtoonService.createWebtoon(
        WebtoonRequest.builder().build());

    ReviewRecordRequest createReviewRecordRequest = ReviewRecordRequest.builder()
        .memberId(memberId)
        .reviewId(null)
        .record("리뷰 없는 기록 저장 테스트")
        .toonId(webtoonCreateResponse.getId())
        .build();

    //When
    CreateReviewRecordResponse createReviewRecordResponse = reviewRecordService.createReviewRecord(
        createReviewRecordRequest);

    //Then
    assertThat(createReviewRecordResponse).isNotNull();
    assertThat(createReviewRecordResponse.getReviewId()).isEqualTo(1L);
    assertThat(createReviewRecordResponse.getRecord()).isEqualTo("리뷰 없는 기록 저장 테스트");

    verify(reviewService).createDefaultReview(
        createReviewRecordRequest.getMemberId(),
        createReviewRecordRequest.getToonId());
  }

  @DisplayName("기록을 수정할 수 있다.")
  @Test
  public void updateReviewRecord() {
    //Given
    Long reviewRecordId = 1L;
    ReviewRecord existReviewRecord = ReviewRecord.builder().record("기존에 있는 리뷰 기록").build();

    given(reviewRecordRepository.findById(reviewRecordId)).willReturn(
        Optional.of(existReviewRecord));

    ReviewRecordRequest updateRequest = ReviewRecordRequest.builder()
        .reviewRecordId(reviewRecordId)
        .record("변경된 리뷰 기록").build();

    //When
    reviewRecordService.updateReviewRecord(updateRequest);

    //Then
    assertThat(existReviewRecord.getRecord()).isEqualTo("변경된 리뷰 기록");
    verify(reviewRecordRepository, only()).findById(reviewRecordId);
  }

  @DisplayName("기록을 삭제 할 수 있다.")
  @Test
  public void deleteReviewRecord() {
    //Given
    Long reviewRecordId = 1L;
    ReviewRecord existReviewRecord = ReviewRecord.builder().build();
    given((reviewRecordRepository).findById(reviewRecordId))
        .willReturn(Optional.of(existReviewRecord));

    //When
    reviewRecordService.deleteReviewRecord(reviewRecordId);

    //Then
    verify(reviewRecordRepository).findById(reviewRecordId);
    verify(reviewRecordRepository).delete(existReviewRecord);
  }

  @DisplayName("기록을 조회 할 수 있다.")
  @Test
  public void findReviewRecord() {
    //Given
    Long reviewId = 1L;
    given(reviewRecordRepository.findByReviewReviewId(reviewId))
        .willReturn(
            Optional.of(List.of(
                ReviewRecord.builder().review(any(Review.class)).record("테스트 기록").build())));
    //When
    List<ReviewRecordResponse> reviewRecordResponse = reviewRecordService.findByReviewId(reviewId);
    //Then
    assertThat(reviewRecordResponse).hasSize(1).extracting("record").containsExactly("테스트 기록");
  }

  @DisplayName("기록이 없는 경우 RecordNotFoundException 예외 발생")
  @Test
  public void findReviewRecordWithException() {
    //Given
    Long reviewId = 1L;
    given(reviewRecordRepository.findByReviewReviewId(reviewId))
        .willReturn(Optional.empty());

    //When & Then
    assertThatThrownBy(() -> reviewRecordService.findByReviewId(reviewId))
        .isInstanceOf(ReviewException.RecordNotFoundException.class)
        .hasMessageContaining("해당 기록이 존재하지 않습니다.");
  }
}