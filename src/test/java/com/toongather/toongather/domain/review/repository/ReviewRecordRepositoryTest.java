package com.toongather.toongather.domain.review.repository;

import static com.toongather.toongather.domain.webtoon.domain.Age.ALL;
import static com.toongather.toongather.domain.webtoon.domain.Platform.LEZHIN;
import static com.toongather.toongather.domain.webtoon.domain.WebtoonStatus.ING;
import static org.assertj.core.api.Assertions.*;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.JoinFormRequest;
import com.toongather.toongather.domain.member.service.EmailService;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewRecord;
import com.toongather.toongather.domain.review.service.ReviewRecordService;
import com.toongather.toongather.domain.review.service.ReviewService;
import com.toongather.toongather.domain.webtoon.domain.GenreKeyword;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.dto.WebtoonCreateResponse;
import com.toongather.toongather.domain.webtoon.dto.WebtoonRequest;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;
import com.toongather.toongather.domain.webtoon.service.WebtoonService;
import groovy.util.logging.Slf4j;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ReviewRecordRepositoryTest {

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private MemberService memberService;

  @Mock
  private EmailService emailService;

  @Autowired
  private ReviewRecordRepository reviewRecordRepository;

  @Autowired
  private ReviewRecordService reviewRecordService;

  @Autowired
  private WebtoonService webtoonService;

  @Autowired
  private WebtoonRepository webtoonRepository;

  private Long reviewId = -1L;

  @BeforeEach
  public void setup() {
    JoinFormRequest joinForm = new JoinFormRequest();
    joinForm.setName("test");
    joinForm.setPhone("010-1234-5678");
    joinForm.setEmail("younie.jang@gmail.com");
    joinForm.setNickName("test");
    joinForm.setPassword("1234");
    Member member = memberService.join(joinForm);

    Member findmember = memberService.findMemberEntityById(member.getId());
    List<GenreKeyword> genreKeywords = List.of(
        createGenreKeyword(1L, "로맨스판타지", "Y"),
        createGenreKeyword(2L, "드라마", "Y")
    );
    WebtoonRequest request = WebtoonRequest.builder()
        .age(ALL).author("홍길동").genreKeywordIds(List.of(1L, 2L)).platform(LEZHIN).summary("테스트 줄거리")
        .status(ING).title("테스트웹툰").build();
    WebtoonCreateResponse webtoonCreateResponse = webtoonService.createWebtoon(request);
    Webtoon webtoon = webtoonRepository.findById(webtoonCreateResponse.getId()).get();

    Review review = Review.createReview(findmember, webtoon, "테스트 추천 코멘트", 5L);
    reviewId = reviewService.saveReview(review);
    Review savedReview = reviewService.findById(reviewId);

    ReviewRecord reviewRecord = ReviewRecord.builder()
        .review(savedReview).record("테스트 기록1").build();
    ReviewRecord reviewRecord2 = ReviewRecord.builder()
        .review(savedReview).record("테스트 기록2").build();

    reviewRecordService.saveReviewRecord(reviewRecord);
    reviewRecordService.saveReviewRecord(reviewRecord2);
  }

  @DisplayName("리뷰 아이디로 기록을 조회 할 수 있다.")
  @Test
  public void findByReviewId() {
    //When &Then
    List<ReviewRecord> result = reviewRecordRepository.findByReviewReviewId(reviewId).get();
    assertThat(result).hasSize(2).extracting("record")
        .containsExactlyInAnyOrder("테스트 기록1", "테스트 기록2");
  }

  private GenreKeyword createGenreKeyword(Long genreKeywordId, String genreKeywordNm, String flag) {
    return GenreKeyword.builder()
        .genreKeywordId(genreKeywordId)
        .genreKeywordNm(genreKeywordNm)
        .flag(flag)
        .build();
  }
}