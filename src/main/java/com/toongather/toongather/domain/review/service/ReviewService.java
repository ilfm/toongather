package com.toongather.toongather.domain.review.service;


import com.toongather.toongather.domain.keyword.domain.Keyword;
import com.toongather.toongather.domain.keyword.repository.KeywordRepository;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewKeyword;
import com.toongather.toongather.domain.review.domain.ReviewRecord;
import com.toongather.toongather.domain.review.domain.ReviewSortType;
import com.toongather.toongather.domain.review.dto.CreateReviewRecordRequest;
import com.toongather.toongather.domain.review.dto.ReviewSearchDto;
import com.toongather.toongather.domain.review.dto.ReviewKeywordDto;
import com.toongather.toongather.domain.review.dto.ReviewRecordDto;
import com.toongather.toongather.domain.review.dto.UpdateReviewRequest;
import com.toongather.toongather.domain.review.repository.ReviewJpaRepository;
import com.toongather.toongather.domain.review.repository.ReviewKeywordRepository;
import com.toongather.toongather.domain.review.repository.ReviewRecordRepository;
import com.toongather.toongather.domain.review.repository.ReviewRepository;
import com.toongather.toongather.global.common.util.file.FileStore;
import com.toongather.toongather.global.common.util.file.UploadFile;

import java.io.IOException;
import java.util.ArrayList;
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
  private ReviewJpaRepository reviewJpaRepository;

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private ReviewRecordRepository reviewRecordRepository;

  @Autowired
  private ReviewKeywordRepository reviewKeywordRepository;

  @Autowired
  private KeywordRepository keywordRepository;

  //@Autowired
  //private FileService fileService;

  private final FileStore fileStore;
  private final int REVIEW_PAGE_SIZE = 10;

  /**
   * 정렬 기준에 따라 리뷰 목록을 조회하는 메서드
   *
   * @param reviewSortType 정렬 기준 (STAR_ASC, STAR_DESC, CREATE_DATE_DESC 등)
   * @param pageable 페이지네이션 정보 (페이지 번호, 크기 등)
   * @return 정렬된 리뷰 목록 (페이지네이션 적용)
   *
   * @implNote
   * - 정렬 기준이 null이면 기본값으로 최신 등록일순(CREATE_DATE_DESC)을 적용.
   * - Pageable의 페이지 번호를 유지하면서, 지정된 REVIEW_PAGE_SIZE를 적용하여 페이지네이션 수행.
   * - Review 엔티티를 ReviewSearchDto로 변환하여 반환.
   */
  public Page<ReviewSearchDto> findAllWithSortType(ReviewSortType reviewSortType,Pageable pageable){

    Sort sort;
    if(reviewSortType == null){
      sort = Sort.by(Direction.DESC, "regDt");
    }else {
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
    Pageable pageRequest = PageRequest.of(pageable.getPageNumber(),REVIEW_PAGE_SIZE,sort);   // TODO 페이지 추후변경

    return reviewRepository.findAll(pageRequest)
        .map(review -> ReviewSearchDto.builder()
            .title(review.getWebtoon().getTitle())
            .recommendComment(review.getRecommendComment())
            .imgPath(review.getWebtoon().getImgPath())
            .star(review.getStar())
            .reviewDate(review.getRegDt())
            .build());
  }

  /**
   * 리뷰 등록
   */
  @Transactional
  public void saveReview(Review review) {
    reviewJpaRepository.save(review);
  }

  /**
   * 해당 리뷰 찾기
   */
  public ReviewSearchDto findOneReview(String reviewId) {
    return reviewJpaRepository.findReview(reviewId);
  }

  /**
   * 마이리뷰 나의기록 등록 - 리뷰가 있으면 -> 나의 기록 등록 - 리뷰가 없으면 -> 리뷰 등록 후 나의 기록 등록
   */
  @Transactional
  public ReviewRecordDto createReviewRecord(CreateReviewRecordRequest request) throws IOException {

    System.out.println("request = " + request.getToonId());
    // 나의 기록 있는 리뷰 있는지 웹툰id로 확인
    Review review = reviewJpaRepository.findByToonId(request.getToonId());

    if (review.getReviewId() == null) {

      return null;
    } else {

      // 나의기록 등록
      ReviewRecord record = ReviewRecord.builder().record(request.getRecord()).review(review)
          .build();
      String reviewRecordId = reviewRecordRepository.saveReviewRecord(record);
      ReviewRecord reviewRecord = reviewRecordRepository.findById(reviewRecordId);

      // 파일 업로드
      List<UploadFile> uploadFiles = fileStore.storeFiles(request.getFileList());

//      List<File> fileList = new ArrayList<>();
//      // 파일 정보 저장
//      for (UploadFile uploadFile : uploadFiles) {
//        fileList.add(File.builder().filePath(uploadFile.getFilePath())
//            .uploadFileName(uploadFile.getUploadFileName())
//            .storeFileName(uploadFile.getStoreFileName())
//            .flag(FileFlag.R).build());
//      }
//      fileService.saveFiles(fileList);

      return ReviewRecordDto.builder().reviewRecordId(reviewRecord.getReviewRecordId())
          .record(reviewRecord.getRecord())
          .reviewId(reviewRecord.getReview().getReviewId()).amdDt(reviewRecord.getAmdDt()).build();

    }
  }

  /*
   * 나의 기록 리뷰 리스트 조회
   * */
  public List<ReviewRecordDto> findReviewRecordList(String reviewId) {
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

  /*
   * 리뷰 키워드 불러오기
   * */
  public List<ReviewKeywordDto> findMyKeywordByReviewId(String reviewId) {
    return reviewKeywordRepository.findMyKeywordByReviewId(reviewId);
  }

  /*
   * 나의 키워드 등록
   * */
  @Transactional
  public ReviewKeywordDto registerMyKeyword(String keywordNm, String reviewId) {

    // 키워드 있으면 불러오기 없으면 저장

    // 키워드 저장
    String keywordId = keywordRepository.save(Keyword.builder().keywordNm(keywordNm).build());
    // 키워드 불러오기
    Keyword keyword = keywordRepository.findByKeywordId(keywordId);
    // 리뷰 불러오기
    Review review = reviewJpaRepository.findById(reviewId);
    // 리뷰 키워드 저장
    String reviewKeywordId = reviewKeywordRepository.save(
        ReviewKeyword.builder().keyword(keyword).review(review).build());

    return ReviewKeywordDto.builder().keywordId(keywordId).reviewKeywordId(reviewKeywordId)
        .keywordNm(keywordNm).build();
  }

  /*
   *  친구에게 추천평 남기기
   * */
  public void updateReview(UpdateReviewRequest request) {

    Review review = reviewJpaRepository.findById(request.getReviewId());
    review.setRecommendComment(request.getRecommendComment());
    reviewJpaRepository.save(review);
  }

}
