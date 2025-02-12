package com.toongather.toongather.domain.review.dto;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;




@Getter
@Setter
public class CreateReviewRecordRequest{
  private Long toonId;
  private Long reviewId;
  private String record;
  private List<MultipartFile> fileList;

  public CreateReviewRecordRequest() {
  }

  @Builder
  public CreateReviewRecordRequest(Long toonId, Long reviewId, String record) {
    this.toonId = toonId;
    this.reviewId = reviewId;
    this.record = record;
  }
}
