package com.toongather.toongather.domain.review.repository;

import static org.assertj.core.api.Assertions.*;


import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.JoinFormRequest;
import com.toongather.toongather.domain.member.service.EmailService;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.domain.review.dto.CreateReviewRequest;
import com.toongather.toongather.domain.review.service.ReviewService;
import com.toongather.toongather.domain.webtoon.domain.Age;
import com.toongather.toongather.domain.webtoon.domain.Platform;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.domain.WebtoonStatus;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
class ReviewKeywordRepositoryTest {

  @Autowired
  ReviewKeywordRepository reviewKeywordRepository;
  @Autowired
  WebtoonRepository webtoonRepository;
  @Autowired
  MemberService memberService;
  @MockitoBean
  EmailService emailService;
  @Autowired
  ReviewService reviewService;

  @Transactional
  @DisplayName("리뷰 아이디로 reviewKeyword 존재 여부를 확인할 수 있다.")
  @Test
  public void existsByReviewId() {
    //Given
    Long memberId = createTestMember();
    Webtoon w1 = createWebtoon("집이없어", 1L);
    webtoonRepository.save(w1);
    List<String> keywords = new ArrayList<>(List.of("혐관", "학원물"));

    CreateReviewRequest createRequest = CreateReviewRequest.builder()
        .memberId(memberId)
        .star(5L)
        .toonId(w1.getToonId())
        .recommendComment("재밌구만유")
        .build();

    Long reviewId = reviewService.createReview(createRequest);
    Long nonExistentReviewId = -1L;

    //When
    boolean result = reviewKeywordRepository.existsByReviewReviewId(reviewId);
    boolean nonExistentResult = reviewKeywordRepository.existsByReviewReviewId(nonExistentReviewId);

    //Then
    assertThat(result).isTrue();
    assertThat(nonExistentResult).isFalse();
  }

  private Long createTestMember() {
    JoinFormRequest joinFormDTO = new JoinFormRequest();
    joinFormDTO.setEmail("ddddd@naver.com");
    joinFormDTO.setName("테스트");
    joinFormDTO.setNickName("테스트닉네임");
    joinFormDTO.setPassword("1234");
    joinFormDTO.setPhone("010-7666-1111");
    Member member = memberService.join(joinFormDTO);
    return member.getId();
  }

  private Webtoon createWebtoon(String title, Long toonId) {
    return Webtoon.builder().title(title).toonId(toonId).author("작가").age(Age.ALL).imgPath(
            "https://image-comic.pstatic.net/webtoon/721433/thumbnail/thumbnail_IMAG21_c907f727-e522-4517-952e-398ea95d2efb.jpg")
        .platform(Platform.NAVER).status(WebtoonStatus.END).build();
  }
}