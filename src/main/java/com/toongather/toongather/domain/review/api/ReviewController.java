package com.toongather.toongather.domain.review.api;


import com.toongather.toongather.domain.review.domain.ReviewSortType;
import com.toongather.toongather.domain.review.dto.CreateReviewRequest;
import com.toongather.toongather.domain.review.dto.SearchReviewResponse;
import com.toongather.toongather.domain.review.dto.UpdateReviewRequest;
import com.toongather.toongather.domain.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/reviews")
@RequiredArgsConstructor
@RestController
public class ReviewController {

  @Autowired
  private ReviewService reviewService;

  @Value("${file.dir}")
  private String fileDir;

  @GetMapping
  public Page<SearchReviewResponse> findAllWithSortType(
      @RequestParam(required = false, defaultValue = "CREATE_DATE_DESC") ReviewSortType sort,
      Pageable pageable) {
    return reviewService.findAllWithSortType(sort, pageable);
  }

  @PostMapping("/new")
  public Long saveReview(@RequestBody CreateReviewRequest request) {
    return reviewService.createReview(request);
  }

  @PostMapping("/{reviewId}")
  public void updateReview(@RequestBody UpdateReviewRequest request, @PathVariable Long reviewId) {
    reviewService.updateReview(request);
  }
}
