package com.toongather.toongather.domain.review.service;

import static org.assertj.core.api.Assertions.*;

import com.toongather.toongather.domain.keyword.dto.KeywordDto;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.JoinFormDTO;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewSortType;
import com.toongather.toongather.domain.review.dto.CreateReviewRequest;
import com.toongather.toongather.domain.review.dto.ReviewDto;
import com.toongather.toongather.domain.review.dto.UpdateReviewRequest;

import com.toongather.toongather.domain.review.repository.ReviewJpaRepository;
import com.toongather.toongather.domain.webtoon.domain.Age;
import com.toongather.toongather.domain.webtoon.domain.Platform;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.domain.WebtoonStatus;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

@Slf4j
@SpringBootTest
@Transactional
@Rollback(false)
public class ReviewServiceTest {

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private MemberService memberService;

  @Autowired
  private WebtoonRepository webtoonRepository;

  @Autowired
  private ReviewJpaRepository reviewJpaRepository;


  @Autowired
  private ReviewKeywordService reviewKeywordService;

  @DisplayName("작성한 리뷰를 정렬타입에 따라서 조회 할 수 있다.")
  @Rollback(true)
  @Test
  void searchWithSortType() throws ParseException {
    //Given
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    JoinFormDTO joinFormDTO = new JoinFormDTO();
    joinFormDTO.setEmail("ddddd@naver.com");
    joinFormDTO.setName("테스트");
    joinFormDTO.setNickName("테스트닉네임");
    joinFormDTO.setPassword("1234");
    joinFormDTO.setPhone("010-7666-1111");
    Long memberId = memberService.join(joinFormDTO);
    Member member = memberService.findMemberEntityById(memberId);

    Webtoon w1 = createWebtoon("집이없어", 1L);
    Webtoon w2 = createWebtoon("집이있어", 2L);
    webtoonRepository.save(w1);
    webtoonRepository.save(w2);

    Review r1 = Review.builder()
        .member(member)
        .star(5L)
        .recommendComment("넘잼")
        .toon(w1)
        .build();
    reviewJpaRepository.save(r1);

    reviewJpaRepository.flush();

    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


    Review r2 = Review.builder().member(member)

        .star(4L)
        .recommendComment("넘잼")
        .toon(w2)
        .build();
    reviewJpaRepository.save(r2);

    Pageable pageRequest = PageRequest.of(0, 10);   // TODO 페이지 추후변경

    //When
    ReviewSortType starAscSort = ReviewSortType.STAR_ASC;
    List<ReviewDto> statAscResult = reviewService.findAllWithSortType(starAscSort, pageRequest)
        .getContent();

    ReviewSortType starDescSort = ReviewSortType.STAR_DESC;
    List<ReviewDto> statDescResult = reviewService.findAllWithSortType(starDescSort, pageRequest)
        .getContent();

    ReviewSortType createDtDescSort = ReviewSortType.CREATE_DATE_DESC;
    List<ReviewDto> createDtDescResult = reviewService.findAllWithSortType(createDtDescSort,
        pageRequest).getContent();

    LocalDateTime firstDate = LocalDateTime.parse(createDtDescResult.get(0).getReviewDate(),
        formatter);
    LocalDateTime secondDate = LocalDateTime.parse(createDtDescResult.get(1).getReviewDate(),
        formatter);

    assertThat(firstDate).isAfter(secondDate);

    //Then    
    assertThat(statAscResult).as("별점 낮은순으로 정렬된다.").hasSize(2).extracting("star")
        .containsExactly(4L, 5L);
    assertThat(statDescResult).as("별점 높은순으로 정렬된다.").hasSize(2).extracting("star")
        .containsExactly(5L, 4L);
    assertThat(firstDate).isAfter(secondDate);
  }

  @DisplayName("키워드를 포함한 리뷰를 등록 할수 있다.")
  @Rollback(true)
  @Test
  void createReview() {
    //Given
    JoinFormDTO joinFormDTO = new JoinFormDTO();
    joinFormDTO.setEmail("ddddd@naver.com");
    joinFormDTO.setName("테스트");
    joinFormDTO.setNickName("테스트닉네임");
    joinFormDTO.setPassword("1234");
    joinFormDTO.setPhone("010-7666-1111");
    Long memberId = memberService.join(joinFormDTO);

    Webtoon w1 = createWebtoon("집이없어", 1L);
    webtoonRepository.save(w1);

    List<String> keywords = new ArrayList<>(List.of("혐관", "학원물"));

    CreateReviewRequest request = CreateReviewRequest.builder()
        .memberId(memberId)
        .star(5L)
        .toonId(w1.getToonId())
        .recommendComment("재밌구만유")
        .keywords(keywords)
        .build();

    //When
    Long reviewId = reviewService.createReview(request);
    ReviewDto reviewDto = reviewService.findById(reviewId);
    List<KeywordDto> findKeyword = reviewKeywordService.getKeywordsByReviewId(reviewId);

    //Then
    Assertions.assertThat(reviewId).isNotNull();
    Assertions.assertThat(reviewDto).extracting("star", "recommendComment").contains(5L, "재밌구만유");
    Assertions.assertThat(findKeyword)
        .hasSize(2)
        .extracting(KeywordDto::getKeywordNm)
        .containsExactlyInAnyOrder("혐관", "학원물");
  }

  @DisplayName("리뷰를 수정 할 수 있다.")
  @Rollback
  @Test
  void updateReview() {
    //Given
    JoinFormDTO joinFormDTO = new JoinFormDTO();
    joinFormDTO.setEmail("ddddd@naver.com");
    joinFormDTO.setName("테스트");
    joinFormDTO.setNickName("테스트닉네임");
    joinFormDTO.setPassword("1234");
    joinFormDTO.setPhone("010-7666-1111");
    Long memberId = memberService.join(joinFormDTO);
    Member member = memberService.findMemberEntityById(memberId);

    Webtoon w1 = createWebtoon("집이없어", 1L);
    webtoonRepository.save(w1);

    List<String> prevKeywords = new ArrayList<>(List.of("혐관", "학원물"));
    List<String> updateKeywords = new ArrayList<>(List.of("호러", "성장물"));

    CreateReviewRequest createRequest = CreateReviewRequest.builder()
        .memberId(memberId)
        .star(5L)
        .toonId(w1.getToonId())
        .recommendComment("재밌구만유")
        .keywords(prevKeywords)
        .build();

    //When
    Long reviewId = reviewService.createReview(createRequest);

    UpdateReviewRequest updateRequest = UpdateReviewRequest.builder()
        .reviewId(reviewId)
        .recommendComment("추천안함")
        .star(2L)
        .keywords(updateKeywords)
        .build();

    reviewService.updateReview(updateRequest);
    ReviewDto reviewDto = reviewService.findById(reviewId);
    List<KeywordDto> findKeyword = reviewKeywordService.getKeywordsByReviewId(reviewId);

    //Then
    Assertions.assertThat(reviewDto).extracting("star", "recommendComment").contains(2L, "추천안함");
    Assertions.assertThat(findKeyword)
        .hasSize(2)
        .extracting(KeywordDto::getKeywordNm)
        .containsExactlyInAnyOrder("호러", "성장물");
  }

  private Webtoon createWebtoon(String title, Long toonId) {
    return Webtoon.builder().title(title).toonId(toonId).author("작가").age(Age.ALL).imgPath(
            "https://image-comic.pstatic.net/webtoon/721433/thumbnail/thumbnail_IMAG21_c907f727-e522-4517-952e-398ea95d2efb.jpg")
        .platform(Platform.NAVER).status(WebtoonStatus.END).build();

  }
}