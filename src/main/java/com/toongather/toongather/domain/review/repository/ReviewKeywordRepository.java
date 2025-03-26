package com.toongather.toongather.domain.review.repository;

import com.toongather.toongather.domain.keyword.domain.Keyword;
import com.toongather.toongather.domain.keyword.dto.KeywordDto;
import com.toongather.toongather.domain.review.domain.ReviewKeyword;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewKeywordRepository extends JpaRepository<ReviewKeyword, Long> {

  @Query(
      "select new com.toongather.toongather.domain.keyword.dto.KeywordDto(k.keywordId,k.keywordNm)"
          + " from ReviewKeyword rk"
          + " join rk.keyword k"
          + " join rk.review r"
          + " where r.reviewId = :reviewId")
  public List<KeywordDto> getKeywordsByReviewId(@Param("reviewId") Long reviewId);

  public boolean existsByReviewReviewId(Long reviewId);

  public List<ReviewKeyword> findByReviewReviewId(Long reviewId);

  public void deleteByReviewReviewId(Long reviewId);
}
