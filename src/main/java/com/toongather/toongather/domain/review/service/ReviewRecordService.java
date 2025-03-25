package com.toongather.toongather.domain.review.service;

import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewRecord;
import com.toongather.toongather.domain.review.dto.CreateReviewRecordResponse;
import com.toongather.toongather.domain.review.dto.ReviewRecordRequest;
import com.toongather.toongather.domain.review.dto.ReviewRecordResponse;
import com.toongather.toongather.domain.review.repository.ReviewRecordRepository;
import com.toongather.toongather.global.common.error.custom.ReviewException.RecordNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReviewRecordService {

  @Autowired
  private ReviewRecordRepository reviewRecordRepository;

  @Autowired
  private ReviewService reviewService;

  public ReviewRecord findById(Long reviewRecordId) {
    return reviewRecordRepository.findById(reviewRecordId)
        .orElseThrow(() -> new RecordNotFoundException());
  }

  public List<ReviewRecordResponse> findByReviewId(Long reviewId) {
    List<ReviewRecord> reviewRecords = reviewRecordRepository.findByReviewReviewId(reviewId)
        .orElseThrow(() -> new RecordNotFoundException());

    return reviewRecords.stream().map(reviewRecord -> ReviewRecordResponse.from(reviewRecord))
        .collect(Collectors.toList());
  }

  @Transactional
  public ReviewRecord saveReviewRecord(ReviewRecord reviewRecord) {
    return reviewRecordRepository.save(reviewRecord);
  }

  @Transactional
  public CreateReviewRecordResponse createReviewRecord(ReviewRecordRequest request) {
    Review review;
    if (request.getReviewId() == null) {
      review = reviewService.createDefaultReview(request.getMemberId(), request.getToonId());
    } else {
      review = reviewService.findById(request.getReviewId());
    }

    ReviewRecord reviewRecord = request.toEntity(review);
    saveReviewRecord(reviewRecord);
    return CreateReviewRecordResponse.builder()
        .reviewId(reviewRecord.getReview().getReviewId())
        .memberId(request.getMemberId())
        .reviewRecordId(reviewRecord.getReviewRecordId())
        .record(reviewRecord.getRecord())
        .build();
  }

  @Transactional
  public void updateReviewRecord(ReviewRecordRequest request) {
    ReviewRecord reviewRecord = findById(request.getReviewRecordId());
    reviewRecord.updateReviewRecord(request.getRecord());
  }

  @Transactional
  public void deleteReviewRecord(Long reviewRecordId) {
    ReviewRecord reviewRecord = findById(reviewRecordId);
    reviewRecordRepository.delete(reviewRecord);
  }
}
