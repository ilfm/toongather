package com.toongather.toongather.domain.review.api;


import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.dto.ReviewDto;
import com.toongather.toongather.domain.review.service.ReviewService;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;
import java.util.List;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/review")
@RequiredArgsConstructor
@RestController
public class ReviewController {

  @Autowired
  private  ReviewService reviewService;
  @Autowired
  private  MemberRepository memberRepository;
  @Autowired
  private  WebtoonRepository webtoonRepository;


  // 마이리뷰 조회
  @PostMapping("/findAllReview")
  public List<ReviewDto> findAllReview() {
    return reviewService.findAllReview();
  }
/*
  // 리뷰 등록
  @Transactional
  @PostMapping("/saveReview")
  public String saveReview(@RequestBody ReviewDto reviewData) {

    log.debug("reviewData.getMemberNo() ={}", reviewData.getMember().getId());
    log.debug("reviewData.getWebtoonId() ={}", reviewData.getWebtoon().getToonId());
    // 멤버, 웹툰 엔티티조회
    Member member = memberRepository.find(reviewData.getMember().getId());
    Webtoon webtoon = webtoonRepository.findById(reviewData.getWebtoon().getToonId());

    // 리뷰 생성
    Review review = Review.createReview(member, webtoon, reviewData);

    // 리뷰 저장
    reviewService.saveReview(review);
    return review.getReviewId();
  }

 */

  // 리뷰 상세 수정 todo 문서참고해서
  
  // 리뷰 상세 가져오기
  @GetMapping("/{reviewId}")
  public void getReviewDetail(@PathVariable String reviewId) {

  }


}