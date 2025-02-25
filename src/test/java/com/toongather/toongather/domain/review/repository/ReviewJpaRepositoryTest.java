package com.toongather.toongather.domain.review.repository;


import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.dto.JoinFormDTO;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewSortType;
import com.toongather.toongather.domain.review.dto.ReviewDto;
import com.toongather.toongather.domain.webtoon.domain.Age;
import com.toongather.toongather.domain.webtoon.domain.Platform;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.domain.WebtoonStatus;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@Rollback(false)
public class ReviewJpaRepositoryTest {

  @PersistenceContext
  private EntityManager em;

  @Autowired
  private ReviewJpaRepository reviewJpaRepository;

  @Autowired
  private MemberService memberService;

  @Autowired
  private WebtoonRepository webtoonRepository;

  @DisplayName("[jpql] 작성한 리뷰를 정렬타입에 따라서 조회 할 수 있다.")
  @Rollback(true)
  @Test
  public void searchWithSortTypeJpql() throws ParseException {
    //Given
    JoinFormDTO joinFormDTO = new JoinFormDTO();
    joinFormDTO.setEmail("ddddd@naver.com");
    joinFormDTO.setName("테스트");
    joinFormDTO.setNickName("테스트닉네임");
    joinFormDTO.setPassword("1234");
    joinFormDTO.setPhone("010-7666-1111");
    Long memberId = memberService.join(joinFormDTO);
    Member m1 = memberService.findMemberEntityById(memberId);

    Webtoon w1 = Webtoon.builder()
        .title("집이 없어")
        .age(Age.ALL)
        .author("와난")
        .status(WebtoonStatus.ING)
        .platform(Platform.NAVER)
        .imgPath(
            "https://image-comic.pstatic.net/webtoon/721433/thumbnail/thumbnail_IMAG21_c907f727-e522-4517-952e-398ea95d2efb.jpg")
        .build();
    webtoonRepository.save(w1);

    Webtoon w2 = Webtoon.builder()
        .title("집이 있어")
        .age(Age.ALL)
        .author("와난")
        .status(WebtoonStatus.ING)
        .platform(Platform.NAVER)
        .imgPath(
            "https://image-comic.pstatic.net/webtoon/721433/thumbnail/thumbnail_IMAG21_c907f727-e522-4517-952e-398ea95d2efb.jpg")
        .build();
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

    //When
    ReviewSortType starAscSort = ReviewSortType.STAR_ASC;
    List<ReviewDto> statAscResult = reviewJpaRepository.findAllWithSortType(starAscSort);

    ReviewSortType starDescSort = ReviewSortType.STAR_DESC;
    List<ReviewDto> statDescResult = reviewJpaRepository.findAllWithSortType(starDescSort);

    ReviewSortType createDtDescSort = ReviewSortType.CREATE_DATE_DESC;
    List<ReviewDto> createDtDescResult = reviewJpaRepository.findAllWithSortType(createDtDescSort);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//    for (ReviewSearchDto dto: createDtDescResult) {
//      System.out.println("date : "+ dto.getReviewDate());
//      System.out.println("date : "+ sdf.parse(dto.getReviewDate()));
//    };

    //Then
//    System.out.println("star 1: "+statAscResult.get(0).getStar());
//    System.out.println("star 2: "+statDescResult.get(0).getStar());
    Assertions.assertThat(statAscResult.get(0).getStar()).isEqualTo(4L);
    Assertions.assertThat(statDescResult.get(0).getStar()).isEqualTo(5L);
    Assertions.assertThat(sdf.parse(createDtDescResult.get(0).getReviewDate()))
        .isAfter(sdf.parse(createDtDescResult.get(1).getReviewDate()));

  }

}