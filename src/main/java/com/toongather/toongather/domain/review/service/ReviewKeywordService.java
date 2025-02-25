package com.toongather.toongather.domain.review.service;

import com.toongather.toongather.domain.keyword.domain.Keyword;
import com.toongather.toongather.domain.keyword.dto.KeywordDto;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewKeyword;
import com.toongather.toongather.domain.review.repository.ReviewKeywordRepository;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ReviewKeywordService {

  private final ReviewKeywordRepository reviewKeywordRepository;

  @Autowired
  public ReviewKeywordService(ReviewKeywordRepository reviewKeywordRepository) {
    this.reviewKeywordRepository = reviewKeywordRepository;
  }

  @Transactional
  public Long saveReviewKeyword(ReviewKeyword reviewKeyword) {
    if (reviewKeyword.getReviewKeywordId() == null) {
      reviewKeywordRepository.save(reviewKeyword);
    }
    return reviewKeyword.getReviewKeywordId();
  }

  @Transactional
  public Long createReviewKeyword(Review review, Keyword keyword) {
    return saveReviewKeyword(ReviewKeyword.createReviewKeyword(review, keyword));
  }

  @Transactional
  public void deleteByReviewId(Long reviewId) {
    reviewKeywordRepository.deleteByReviewReviewId(reviewId);
  }

  public List<KeywordDto> getKeywordsByReviewId(Long reviewId) {
    return reviewKeywordRepository.getKeywordsByReviewId(reviewId);
  }

}
