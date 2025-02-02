package com.toongather.toongather.domain.review.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.repository.MemberRepository;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewSortType;
import com.toongather.toongather.domain.webtoon.domain.Age;
import com.toongather.toongather.domain.webtoon.domain.Platform;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.domain.WebtoonStatus;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(false)
public class ReviewJpaRepositoryTest {

  @Autowired
  private ReviewJpaRepository reviewJpaRepository;

  @Autowired
  private MemberService memberService;

  @Autowired
  private WebtoonRepository webtoonRepository;

  @DisplayName("jpql로 작성한 리뷰를 정렬타입에 따라서 조회 할 수 있다.")
  @Test
  public void searchWithSortType() {
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
        .recomandComment("넘잼")
        .toon(w1)
        .build();
    reviewJpaRepository.save(r1);
    
    Review r2 = Review.builder().member(m1)
        .star(4L)
        .recomandComment("넘잼")
        .toon(w2)
        .build();
    reviewJpaRepository.save(r1);

    ReviewSortType sort = ReviewSortType.STAR_ASC;

    //When
    reviewJpaRepository.searchWithSortType(sort);
    //Then
    //org.assertj.core.api.Assertions.assertThat()

  }

}