package com.toongather.toongather.domain.review.service;


import com.toongather.toongather.domain.keyword.domain.Keyword;
import com.toongather.toongather.domain.keyword.service.KeywordService;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.member.service.MemberService;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewRecord;
import com.toongather.toongather.domain.review.domain.ReviewSortType;
import com.toongather.toongather.domain.review.dto.CreateReviewRequest;
import com.toongather.toongather.domain.review.dto.ReviewDto;
import com.toongather.toongather.domain.review.dto.ReviewRecordDto;
import com.toongather.toongather.domain.review.dto.UpdateReviewRequest;
import com.toongather.toongather.domain.review.repository.ReviewRecordRepository;
import com.toongather.toongather.domain.review.repository.ReviewRepository;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;
import com.toongather.toongather.global.common.util.file.FileStore;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private WebtoonRepository webtoonRepository;

  @Autowired
  private ReviewRecordRepository reviewRecordRepository;

  @Autowired
  private KeywordService keywordService;

  @Autowired
  private MemberService memberService;

  @Autowired
  private ReviewKeywordService reviewKeywordService;

  //@Autowired
  //private FileService fileService;

  private final FileStore fileStore;
  private final int REVIEW_PAGE_SIZE = 10;

  /**
   * 정렬 기준에 따라 리뷰 목록을 조회하는 메서드
   *
   * @param reviewSortType 정렬 기준 (STAR_ASC, STAR_DESC, CREATE_DATE_DESC 등)
   * @param pageable       페이지네이션 정보 (페이지 번호, 크기 등)
   * @return 정렬된 리뷰 목록 (페이지네이션 적용)
   * @implNote - 정렬 기준이 null이면 기본값으로 최신 등록일순(CREATE_DATE_DESC)을 적용. - Pageable의 페이지 번호를 유지하면서, 지정된
   * REVIEW_PAGE_SIZE를 적용하여 페이지네이션 수행. - Review 엔티티를 ReviewDto 변환하여 반환.
   */
  public Page<ReviewDto> findAllWithSortType(ReviewSortType reviewSortType, Pageable pageable) {

    Sort sort;
    if (reviewSortType == null) {
      sort = Sort.by(Direction.DESC, "regDt");
    } else {
      switch (reviewSortType) {
        case STAR_DESC:
          sort = Sort.by(Direction.DESC, "star");
          break;
        case STAR_ASC:
          sort = Sort.by(Direction.ASC, "star");
          break;
        case CREATE_DATE_DESC:
          sort = Sort.by(Direction.DESC, "regDt");
          break;
        default:
          sort = Sort.by(Direction.DESC, "regDt");
          break;
      }
    }
    Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), REVIEW_PAGE_SIZE,
        sort);   // TODO 페이지 추후변경

    return reviewRepository.findAll(pageRequest)
        .map(review -> ReviewDto.builder()
            .title(review.getWebtoon().getTitle())
            .recommendComment(review.getRecommendComment())
            .imgPath(review.getWebtoon().getImgPath())
            .star(review.getStar())
            .reviewDate(review.getRegDt())
            .build());
  }

  /**
   * 리뷰 엔티티 저장 (이미 존재하는 리뷰가 아닐 경우 저장)
   *
   * @param review 저장할 리뷰 엔티티
   * @return 저장된 리뷰의 ID
   */
  @Transactional
  public Long saveReview(Review review) {
    if (review.getReviewId() == null) {
      reviewRepository.save(review);
    }
    return review.getReviewId();
  }

  /**
   * 리뷰 등록
   *
   * @param request 리뷰 등록 요청 객체 (멤버 ID, 웹툰 ID, 리뷰 내용, 별점, 키워드 목록 등 포함)
   * @return 생성된 리뷰의 ID
   */
  @Transactional
  public Long createReview(CreateReviewRequest request) {
    // 멤버, 웹툰 엔티티조회
    Member member = memberService.findMemberEntityById(request.getMemberId());
    Webtoon webtoon = webtoonRepository.findById(request.getToonId());

    // 리뷰 엔티티 생성
    Review review = request.toEntity(member, webtoon);
    Long reviewId = saveReview(review);

    // 키워드 여부 체크
    if (!request.getKeywords().isEmpty()) {
      for (String keywordNm : request.getKeywords()) {
        Keyword keyword = keywordService.createKeyword(keywordNm);
        reviewKeywordService.createReviewKeyword(review, keyword);
      }
    }
    return reviewId;
  }

  /**
   * 특정 리뷰 조회
   *
   * @param reviewId 조회할 리뷰의 ID
   * @return 리뷰 정보를 담은 DTO 객체
   * @throws NoSuchElementException 리뷰가 존재하지 않을 경우 예외 발생
   */
  public ReviewDto findById(Long reviewId) {
    return reviewRepository.findById(reviewId)
        .map(review ->
            ReviewDto.builder()
                .reviewDate(review.getRegDt())
                .recommendComment(review.getRecommendComment())
                .star(review.getStar())
                .memberId(review.getMember().getId())
                .toonId(review.getWebtoon().getToonId())
                //.keywords(review.getKeywords())
                .build())
        .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다: " + reviewId));
  }

  /**
   * 리뷰 수정
   *
   * @param request 리뷰 수정 요청 객체 (수정할 리뷰 ID, 추천 코멘트, 별점, 키워드 목록 포함)
   * @throws NoSuchElementException 수정할 리뷰가 존재하지 않을 경우 예외 발생
   */
  @Transactional
  public void updateReview(UpdateReviewRequest request) {
    Review review = reviewRepository.findById(request.getReviewId())
        .orElseThrow(() -> new NoSuchElementException("리뷰를 찾을 수 없습니다: " + request.getReviewId()));
    review.updateReview(request.getRecommendComment(), request.getStar());

    if (request.getKeywords().isEmpty()) {
      reviewKeywordService.deleteByReviewId(request.getReviewId());
    } else {
      reviewKeywordService.deleteByReviewId(request.getReviewId());
      for (String keywordNm : request.getKeywords()) {
        Keyword keyword = keywordService.createKeyword(keywordNm);
        reviewKeywordService.createReviewKeyword(review, keyword);
      }
    }
  }

  /*
   * 나의 기록 리뷰 리스트 조회
   * */
  public List<ReviewRecordDto> findReviewRecordList(Long reviewId) {
    List<ReviewRecord> reviewRecords = reviewRecordRepository.selectReviewRecordList(reviewId);
    List<ReviewRecordDto> reviewRecordDtos = new ArrayList<>();
    reviewRecords.forEach(reviewRecord -> {
      reviewRecordDtos.add(
          ReviewRecordDto.builder().reviewRecordId(reviewRecord.getReviewRecordId())
              .reviewId(reviewRecord.getReview().getReviewId()).record(reviewRecord.getRecord())
              .amdDt(reviewRecord.getAmdDt())
              .build());
    });

    return reviewRecordDtos;
  }

}
