package com.toongather.toongather.domain.review.repository;

import com.toongather.toongather.domain.review.domain.ReviewRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRecordRepository extends JpaRepository<ReviewRecord, Long> {

}
