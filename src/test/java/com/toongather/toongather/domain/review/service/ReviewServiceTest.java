package com.toongather.toongather.domain.review.service;

import static org.assertj.core.api.Assertions.*;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewSortType;
import com.toongather.toongather.domain.review.dto.ReviewSearchDto;
import com.toongather.toongather.domain.review.repository.ReviewJpaRepository;
import com.toongather.toongather.domain.webtoon.domain.Age;
import com.toongather.toongather.domain.webtoon.domain.Platform;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.domain.WebtoonStatus;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
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

  @DisplayName("작성한 리뷰를 정렬타입에 따라서 조회 할 수 있다.")
  @Rollback(true)
  @Test
  public void searchWithSortType() throws ParseException {
    //Given
    Member m1= Member.builder().name("김동휘").nickName("닉네임").phone("010-2345-1234").email("123@gmail.com").password("123").build();
    memberService.join(m1);

    Webtoon w1 = Webtoon.builder()
        .toonId("1000000721433")
        .title("집이 없어")
        .age(Age.ALL)
        .author("와난")
        .status(WebtoonStatus.ING)
        .platform(Platform.NAVER)
        .imgPath("https://image-comic.pstatic.net/webtoon/721433/thumbnail/thumbnail_IMAG21_c907f727-e522-4517-952e-398ea95d2efb.jpg").build();
    webtoonRepository.save(w1);

    Webtoon w2 = Webtoon.builder()
        .toonId("1000000721432")
        .title("집이 있어")
        .age(Age.ALL)
        .author("와난")
        .status(WebtoonStatus.ING)
        .platform(Platform.NAVER)
        .imgPath("https://image-comic.pstatic.net/webtoon/721433/thumbnail/thumbnail_IMAG21_c907f727-e522-4517-952e-398ea95d2efb.jpg").build();
    webtoonRepository.save(w2);

    Review r1 = Review.builder().member(m1)
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

    Review r2 = Review.builder().member(m1)
        .star(4L)
        .recommendComment("넘잼")
        .toon(w2)
        .build();
    reviewJpaRepository.save(r2);

    Pageable pageRequest = PageRequest.of(0,10);   // TODO 페이지 추후변경
    //When
    ReviewSortType starAscSort = ReviewSortType.STAR_ASC;
    List<ReviewSearchDto> statAscResult = reviewService.findAllWithSortType(starAscSort,pageRequest).getContent();

    ReviewSortType starDescSort = ReviewSortType.STAR_DESC;
    List<ReviewSearchDto> statDescResult = reviewService.findAllWithSortType(starDescSort,pageRequest).getContent();

    ReviewSortType createDtDescSort = ReviewSortType.CREATE_DATE_DESC;
    List<ReviewSearchDto> createDtDescResult = reviewService.findAllWithSortType(createDtDescSort,pageRequest).getContent();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    for (ReviewSearchDto dto: createDtDescResult) {
      System.out.println("date : "+ dto.getReviewDate());
      System.out.println("date : "+ sdf.parse(dto.getReviewDate()));
    };

    //Then
    System.out.println("star 1: "+statAscResult.get(0).getStar());
    System.out.println("star 2: "+statDescResult.get(0).getStar());
    assertThat(statAscResult.get(0).getStar()).isEqualTo(4L);
    assertThat(statDescResult.get(0).getStar()).isEqualTo(5L);
    assertThat(sdf.parse(createDtDescResult.get(0).getReviewDate()))
        .isAfter(sdf.parse(createDtDescResult.get(1).getReviewDate()));

  }
}