package com.toongather.toongather.domain.review.service;

import com.toongather.toongather.domain.keyword.domain.Keyword;
import com.toongather.toongather.domain.keyword.dto.KeywordDto;
import com.toongather.toongather.domain.keyword.service.KeywordService;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewKeyword;
import com.toongather.toongather.domain.review.dto.CreateReviewKeywordRequest;
import com.toongather.toongather.domain.review.repository.ReviewKeywordRepository;
import java.util.ArrayList;
import java.util.List;

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
    List<ReviewKeyword> reviewKeywords = new ArrayList<>();
    for (String keywordNm : request.getKeywords()) {
      Keyword keyword = keywordService.createKeyword(keywordNm);
      Review review = reviewService.findById(request.getReviewId());
      reviewKeywords.add(ReviewKeyword.createReviewKeyword(review, keyword));
    }
    saveAll(reviewKeywords);
  }

  @Transactional
  public void updateReviewKeyword(CreateReviewKeywordRequest request) {
    List<ReviewKeyword> reviewKeywords = new ArrayList<>();
    Review review = reviewService.findById(request.getReviewId());

    List<ReviewKeyword> existingReviewKeywords = reviewKeywordRepository.findByReviewReviewId(
        request.getReviewId());
    reviewKeywordRepository.deleteAll(existingReviewKeywords);

    for (String newKeyword : request.getKeywords()) {
      Keyword keyword = keywordService.createKeyword(newKeyword);
      reviewKeywords.add(ReviewKeyword.createReviewKeyword(review, keyword));
    }
    saveAll(reviewKeywords);
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
