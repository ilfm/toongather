package com.toongather.toongather.domain.review.service;

import com.toongather.toongather.domain.keyword.domain.Keyword;
import com.toongather.toongather.domain.keyword.dto.KeywordDto;
import com.toongather.toongather.domain.keyword.service.KeywordService;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewKeyword;
import com.toongather.toongather.domain.review.dto.CreateReviewKeywordRequest;
import com.toongather.toongather.domain.review.repository.ReviewKeywordRepository;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ReviewKeywordService {

  @Autowired
  private final ReviewKeywordRepository reviewKeywordRepository;
  @Autowired
  private final KeywordService keywordService;
  @Autowired
  private final ReviewService reviewService;


  @Transactional
  private Long saveReviewKeyword(ReviewKeyword reviewKeyword) {
    if (reviewKeyword.getReviewKeywordId() == null) {
      reviewKeywordRepository.save(reviewKeyword);
    }
    return reviewKeyword.getReviewKeywordId();
  }

  @Transactional
  private void saveAll(List<ReviewKeyword> reviewKeywords) {
    reviewKeywordRepository.saveAll(reviewKeywords);
  }

  @Transactional
  public void createReviewKeyword(CreateReviewKeywordRequest request) {
    for (String keywordNm : request.getKeywords()) {
      Keyword keyword = keywordService.createKeyword(keywordNm);
      Review review = reviewService.findEntityById(request.getReviewId());
      saveReviewKeyword(ReviewKeyword.createReviewKeyword(review, keyword));
    }
  }

  @Transactional
  public void updateReviewKeyword(CreateReviewKeywordRequest request) {
    Review review = reviewService.findEntityById(request.getReviewId());
    // 기존 등록된 리뷰 키워드
    List<ReviewKeyword> existingReviewKeywords = reviewKeywordRepository.findByReviewReviewId(
        request.getReviewId());

    // 기존 등록된 키워드명 Set
    Set<String> existingReviewKeywordNm = existingReviewKeywords.stream()
        .map((rk) -> rk.getKeyword().getKeywordNm()).collect(Collectors.toSet());

    // 수정할 키워드명 Set
    Set<String> newKeywordNames = new HashSet<>(request.getKeywords());

    // 삭제할 키워드(기존키워드에 있지만, 수정할 키워드에 없는 키워드)
    List<ReviewKeyword> keywordsToDelete = existingReviewKeywords.stream()
        .filter(rk -> !newKeywordNames.contains(rk.getKeyword().getKeywordNm()))
        .collect(Collectors.toList());

    // 추가할 키워드(수정할 키워드에 있지만, 기존키워드에서는 없던 키워드)
    List<ReviewKeyword> newReviewKeywords = new ArrayList<>();
    for (String keywordNm : newKeywordNames) {
      if (!existingReviewKeywordNm.contains(keywordNm)) {
        Keyword keyword = keywordService.createKeyword(keywordNm);
        newReviewKeywords.add(ReviewKeyword.createReviewKeyword(review, keyword));
      }
    }
    reviewKeywordRepository.deleteAll(keywordsToDelete);
    saveAll(newReviewKeywords);
  }

  @Transactional
  public void deleteByReviewId(Long reviewId) {
    reviewKeywordRepository.deleteByReviewReviewId(reviewId);
  }

  public List<KeywordDto> getKeywordsByReviewId(Long reviewId) {
    return reviewKeywordRepository.getKeywordsByReviewId(reviewId);
  }

  public boolean existsByReviewId(Long reviewId) {
    if (reviewKeywordRepository.existsByReviewReviewId(reviewId)) {
      return false;
    }
    return true;
  }

}
