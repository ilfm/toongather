package com.toongather.toongather.domain.review.api;


import com.toongather.toongather.domain.review.domain.ReviewSortType;
import com.toongather.toongather.domain.review.dto.CreateReviewRequest;
import com.toongather.toongather.domain.review.dto.ReviewDto;
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

  /**
   * 전체 리뷰 조회 API
   *
   * @param sort     정렬 기준 (STAR_ASC, STAR_DESC, CREATE_DATE_DESC 등)
   * @param pageable 페이지네이션 정보
   * @return 정렬된 리뷰 목록
   */
  @GetMapping
  public Page<ReviewDto> findAllWithSortType(
      @RequestParam(required = false, defaultValue = "CREATE_DATE_DESC") ReviewSortType sort,
      Pageable pageable) {
    return reviewService.findAllWithSortType(sort, pageable);
  }

  /**
   * 리뷰 등록 API
   *
   * @param request 리뷰 생성 요청 객체 (리뷰 내용, 별점, 웹툰 ID 등 포함)
   * @return 생성된 리뷰의 ID
   */
  @PostMapping("/new")
  public Long saveReview(@RequestBody CreateReviewRequest request) {
    return reviewService.createReview(request);
  }

  /**
   * 리뷰 수정 API
   *
   * @param request  리뷰 수정 요청 객체 (수정할 리뷰 내용, 별점 등 포함)
   * @param reviewId 수정할 리뷰의 ID (URL 경로 변수)
   */
  @PostMapping("/{reviewId}")
  public void updatteReview(@RequestBody UpdateReviewRequest request, @PathVariable Long reviewId) {
    reviewService.updateReview(request);
  }
}
