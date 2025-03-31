package com.toongather.toongather.domain.review.repository;

import com.toongather.toongather.domain.review.domain.ReviewRecord;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRecordRepository extends JpaRepository<ReviewRecord, Long> {

  public Optional<List<ReviewRecord>> findByReviewReviewId(Long reviewId);
}
