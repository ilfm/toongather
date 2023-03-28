package com.toongather.toongather;


import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.dto.ReviewDto;
import com.toongather.toongather.domain.review.repository.ReviewRepository;
import com.toongather.toongather.domain.review.service.ReviewService;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ReviewRepositoryTest {

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private WebtoonRepository webtoonRepository;


  @Test
  @Transactional
  @Rollback(false)
  public void 리뷰등록() {

    // 멤버 가져오기
    Member findMember = memberRepository.find(1L);
    // 웹툰 가져오기
    Webtoon findWebtoon = webtoonRepository.findById("WT-4");

    Review review = Review.builder()
        .member(findMember)
        .toon(findWebtoon)
        .recomandComment("굳굳")
        .record("굳굳2")
        .star(4L)
        .build();
    reviewRepository.save(review);
  }


  // 모든리뷰 조회
  @Test
  public void 모든리뷰조회() {
    List<ReviewDto> allReview = reviewService.findAllReview();
  }

  // 리뷰 상세 가져오기
  @Test
  public void 리뷰상세가져오기(){

    Review review = reviewService.findOneReview("RV-2");
    System.out.println("review = " + review.getReviewId());

  }


}
