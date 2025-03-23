package com.toongather.toongather.domain.review.service;


import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.toongather.toongather.domain.keyword.dto.KeywordDto;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.JoinFormDTO;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.dto.CreateReviewRequest;
import com.toongather.toongather.domain.review.dto.ReviewDto;

import com.toongather.toongather.domain.review.dto.UpdateReviewRequest;
import com.toongather.toongather.domain.review.repository.ReviewJpaRepository;
import com.toongather.toongather.domain.review.repository.ReviewRepository;
import com.toongather.toongather.domain.webtoon.domain.GenreKeyword;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.dto.WebtoonCreateResponse;
import com.toongather.toongather.domain.webtoon.dto.WebtoonRequest;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;

import com.toongather.toongather.domain.webtoon.service.WebtoonService;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

  @InjectMocks
  private ReviewService reviewService;

  @Mock
  private MemberService memberService;

  @Mock
  private WebtoonRepository webtoonRepository;

  @Mock
  private WebtoonService webtoonService;

  @Mock
  private ReviewJpaRepository reviewJpaRepository;

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private ReviewKeywordService reviewKeywordService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);  // mock 객체 초기화
  }

//  @DisplayName("작성한 리뷰를 정렬타입에 따라서 조회 할 수 있다.")
//  @Rollback(true)
//  @Test
//  void searchWithSortType() throws ParseException {
//    //Given
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    Long memberId = createTestMember();
//    Member member = memberService.findMemberEntityById(memberId);
//
//    Webtoon w1 = createWebtoon("집이없어", 1L);
//    Webtoon w2 = createWebtoon("집이있어", 2L);
//    webtoonRepository.save(w1);
//    webtoonRepository.save(w2);
//
//    Review r1 = Review.builder()
//        .member(member)
//        .star(5L)
//        .recommendComment("넘잼")
//        .toon(w1)
//        .build();
//    reviewJpaRepository.save(r1);
//
//    reviewJpaRepository.flush();
//
//    try {
//      Thread.sleep(2000);
//    } catch (InterruptedException e) {
//      e.printStackTrace();
//    }
//
//    Review r2 = Review.builder().member(member)
//
//        .star(4L)
//        .recommendComment("넘잼")
//        .toon(w2)
//        .build();
//    reviewJpaRepository.save(r2);
//
//    Pageable pageRequest = PageRequest.of(0, 10);   // TODO 페이지 추후변경
//
//    //When
//    ReviewSortType starAscSort = ReviewSortType.STAR_ASC;
//    List<ReviewDto> statAscResult = reviewService.findAllWithSortType(starAscSort, pageRequest)
//        .getContent();
//
//    ReviewSortType starDescSort = ReviewSortType.STAR_DESC;
//    List<ReviewDto> statDescResult = reviewService.findAllWithSortType(starDescSort, pageRequest)
//        .getContent();
//
//    ReviewSortType createDtDescSort = ReviewSortType.CREATE_DATE_DESC;
//    List<ReviewDto> createDtDescResult = reviewService.findAllWithSortType(createDtDescSort,
//        pageRequest).getContent();
//
//    LocalDateTime firstDate = LocalDateTime.parse(createDtDescResult.get(0).getReviewDate(),
//        formatter);
//    LocalDateTime secondDate = LocalDateTime.parse(createDtDescResult.get(1).getReviewDate(),
//        formatter);
//
//    assertThat(firstDate).isAfter(secondDate);
//
//    //Then
//    assertThat(statAscResult).as("별점 낮은순으로 정렬된다.").hasSize(2).extracting("star")
//        .containsExactly(4L, 5L);
//    assertThat(statDescResult).as("별점 높은순으로 정렬된다.").hasSize(2).extracting("star")
//        .containsExactly(5L, 4L);
//    assertThat(firstDate).isAfter(secondDate);
//  }

  @DisplayName("리뷰를 등록 할수 있다.")
  @Test
  void createReview() {
    //Given
    ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
    Long memberId = 1L;
    Long toonId = 1L;
    
    given(memberService.findMemberEntityById(memberId)).willReturn(Member.builder().build());
    given(webtoonRepository.findById(toonId))
        .willReturn(Optional.of(Webtoon.builder().genreKeywords(List.of()).build()));

    CreateReviewRequest request = CreateReviewRequest.builder()
        .memberId(memberId)
        .star(5L)
        .toonId(toonId)
        .recommendComment("테스트 추천 코멘트")
        .build();

    //When
    Long reviewId = reviewService.createReview(request);

    //Then
    verify(reviewRepository, times(1)).save(captor.capture());  // save 메서드 호출 검증
    verify(memberService, times(1)).findMemberEntityById(memberId);
    verify(webtoonRepository, times(1)).findById(toonId);

    assertThat(captor.getAllValues()).isNotEmpty();
    assertThat(captor.getValue().getRecommendComment()).isEqualTo("테스트 추천 코멘트");
    assertThat(captor.getValue().getStar()).isEqualTo(5L);
  }

  //  @DisplayName("기본정보 리뷰 생성")
//  @Test
//  public void createDefaultReview() {
//    //Given
//    given(memberService.join(any(JoinFormDTO.class))).willReturn(1L);
//    given(webtoonService.createWebtoon(any(WebtoonRequest.class))).willReturn(
//        WebtoonCreateResponse.builder().id(1L).build());
//
//    Long memberId = memberService.join(new JoinFormDTO());
//    WebtoonCreateResponse response = webtoonService.createWebtoon(WebtoonRequest.builder().build());
//
//    //When
//    //Review review = reviewService.createDefaultReview(memberId, response.getId());
//    //Then
////    assertThat(review).isNotNull();
////    verify(webtoonRepository).findById(response.getId());
////    verify(memberService).findMemberEntityById(memberId);
//
//  }
//
  @DisplayName("리뷰를 수정 할 수 있다.")
  @Test
  void updateReview() {
    //Given
    Long reviewId = 1L;
    Review existReview = Review.builder().recommendComment("기존 테스트 추천 코멘트").star(5L).build();
    given(reviewRepository.findById(reviewId))
        .willReturn(Optional.of(existReview));
    given(reviewRepository.findById(reviewId)).willReturn(Optional.of(existReview));

    UpdateReviewRequest updateRequest = UpdateReviewRequest.builder()
        .reviewId(reviewId)
        .recommendComment("변경 테스트 추천 코멘트")
        .star(1L)
        .build();

    //When
    reviewService.updateReview(updateRequest);
    Review updateReview = reviewRepository.findById(reviewId).get();

    //Then
    assertThat(updateReview.getStar()).isEqualTo(1L);
    assertThat(updateReview.getRecommendComment()).isEqualTo("변경 테스트 추천 코멘트");

  }

  @DisplayName("리뷰가 있는 경우,리뷰를 삭제 할 수 있다")
  @Rollback
  @Test
  public void deleteReview() {
    //Given
    Long reviewId = 1L;
    given(reviewRepository.existsById(reviewId)).willReturn(true);

    //When
    reviewService.deleteReview(reviewId);

    //then
    verify(reviewRepository, times(1)).deleteById(reviewId);
  }

  @DisplayName("리뷰가 없는 경우, 리뷰 삭제 시 NoSuchElementException 예외발생")
  @Test
  public void deleteReviewNonExistReview() {
    //Given
    Long nonExistentReviewId = 1L;
    given(reviewRepository.existsById(nonExistentReviewId)).willReturn(false);

    //When&then
    assertThatThrownBy(() -> reviewService.deleteReview(nonExistentReviewId))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessageContaining("삭제 할 리뷰가 없습니다.");
  }

}