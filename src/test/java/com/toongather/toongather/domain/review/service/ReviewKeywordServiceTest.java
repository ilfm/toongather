package com.toongather.toongather.domain.review.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

import com.toongather.toongather.domain.keyword.domain.Keyword;
import com.toongather.toongather.domain.keyword.service.KeywordService;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewKeyword;
import com.toongather.toongather.domain.review.dto.CreateReviewKeywordRequest;
import com.toongather.toongather.domain.review.repository.ReviewKeywordRepository;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewKeywordServiceTest {

  @InjectMocks
  ReviewKeywordService reviewKeywordService;
  @Mock
  ReviewKeywordRepository reviewKeywordRepository;
  @Mock
  ReviewService reviewService;
  @Mock
  KeywordService keywordService;

  @DisplayName("리뷰 키워드를 등록 할 수 있다.")
  @Test
  public void createReviewKeyword() {
    //Given
    Long reviewId = 1L;
    List<String> keywords = List.of("혐관", "학원물");
    CreateReviewKeywordRequest request = CreateReviewKeywordRequest.builder()
        .reviewId(reviewId)
        .keywords(keywords).build();

    //When
    reviewKeywordService.createReviewKeyword(request);

    //Then
    verify(keywordService, times(keywords.size())).createKeyword(any(String.class));
    ArgumentCaptor<String> keywordCaptor = ArgumentCaptor.forClass(String.class);
    verify(keywordService, times(keywords.size())).createKeyword(keywordCaptor.capture());
    assertThat(keywordCaptor.getAllValues()).containsExactlyInAnyOrderElementsOf(keywords);
  }


  @DisplayName("리뷰 키워드를 수정 할 수 있다.")
  @Test
  public void updateReviewKeyword() {
    //Given
    Long reviewId = 1L;
    List<String> existingKeywords = List.of("기존의", "키워드");
    List<String> newKeywords = List.of("수정된", "키워드");
    List<ReviewKeyword> existingReviewKeyword = new ArrayList<>();
    for (String keyword : existingKeywords) {
      existingReviewKeyword.add(
          ReviewKeyword.builder().keyword(Keyword.builder().keywordNm(keyword).build()).build());
    }

    CreateReviewKeywordRequest request = CreateReviewKeywordRequest.builder()
        .reviewId(reviewId)
        .keywords(newKeywords)
        .build();

    given(reviewService.findEntityById(request.getReviewId())).willReturn(mock(Review.class));
    given(reviewKeywordRepository.findByReviewReviewId(request.getReviewId()))
        .willReturn(existingReviewKeyword);

    given(keywordService.createKeyword("수정된"))
        .willReturn(Keyword.builder().keywordNm("수정된").build());

    ArgumentCaptor<List<ReviewKeyword>> savedKeywordsCaptor = ArgumentCaptor.forClass(List.class);
    ArgumentCaptor<List<ReviewKeyword>> deletedKeywordsCaptor = ArgumentCaptor.forClass(List.class);

    //When
    reviewKeywordService.updateReviewKeyword(request);

    //Then
    verify(reviewService).findEntityById(reviewId);
    verify(reviewKeywordRepository).findByReviewReviewId(reviewId);
    verify(reviewKeywordRepository).deleteAll(deletedKeywordsCaptor.capture());
    verify(reviewKeywordRepository).saveAll(savedKeywordsCaptor.capture());

    List<ReviewKeyword> deleteKeywords = deletedKeywordsCaptor.getValue();
    List<ReviewKeyword> savedKeywords = savedKeywordsCaptor.getValue();

    assertThat(deleteKeywords).extracting(
            reviewKeyword -> reviewKeyword.getKeyword().getKeywordNm())
        .containsExactlyInAnyOrder("기존의");
    assertThat(savedKeywords).extracting(
            reviewKeyword -> reviewKeyword.getKeyword().getKeywordNm())
        .containsExactlyInAnyOrder("수정된");
  }
}