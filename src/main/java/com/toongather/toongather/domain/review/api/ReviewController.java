package com.toongather.toongather.domain.review.api;


import com.toongather.toongather.domain.review.domain.ReviewSortType;
import com.toongather.toongather.domain.review.dto.CreateReviewRequest;
import com.toongather.toongather.domain.review.dto.SearchReviewResponse;
import com.toongather.toongather.domain.review.dto.UpdateReviewRequest;
import com.toongather.toongather.domain.review.service.ReviewService;
import com.toongather.toongather.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/reviews")
@RequiredArgsConstructor
@RestController
public class ReviewController {

  private final ReviewService reviewService;

  @Value("${file.dir}")
  private String fileDir;

  @GetMapping
  public Page<SearchReviewResponse> findAllWithSortType(
      @RequestParam(required = false, defaultValue = "CREATE_DATE_DESC") ReviewSortType sort,
      Pageable pageable) {
    return reviewService.findAllWithSortType(sort, pageable);
  }

  @PostMapping("/new")
  public ResponseEntity<Long> createReview(@RequestBody CreateReviewRequest request) {
    return ResponseEntity.ok(reviewService.createReview(request));
  }

  @PutMapping("/{reviewId}")
  public ResponseEntity<Void> updateReview(@RequestBody UpdateReviewRequest request,
      @PathVariable Long reviewId) {
    reviewService.updateReview(request);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/{reviewId}")
  public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
    reviewService.deleteReview(reviewId);
    return ResponseEntity.ok().build();
  }
}
