package com.toongather.toongather.domain.review.dto;

import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewRecord;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
public class ReviewRecordRequest {

  private long reviewRecordId;
  private Long memberId;
  private Long toonId;
  private Long reviewId;
  private String record;
  private List<MultipartFile> fileList;

  public ReviewRecordRequest() {
  }

  public ReviewRecord toEntity(Review review) {
    return ReviewRecord.builder()
        .record(this.record)
        .review(review)
        .build();
  }

  @Builder
  public ReviewRecordRequest(Long reviewRecordId, Long memberId, Long toonId, Long reviewId,
      String record) {
    this.reviewRecordId = reviewRecordId;
    this.memberId = memberId;
    this.toonId = toonId;
    this.reviewId = reviewId;
    this.record = record;
  }
}
