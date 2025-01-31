package com.toongather.toongather.domain.review.service;


import com.toongather.toongather.domain.keyword.domain.Keyword;
import com.toongather.toongather.domain.keyword.repository.KeywordRepository;
import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.domain.ReviewKeyword;
import com.toongather.toongather.domain.review.domain.ReviewRecord;
import com.toongather.toongather.domain.review.dto.CreateReviewRecordRequest;
import com.toongather.toongather.domain.review.dto.ReviewDto;
import com.toongather.toongather.domain.review.dto.ReviewKeywordDto;
import com.toongather.toongather.domain.review.dto.ReviewRecordDto;
import com.toongather.toongather.domain.review.dto.UpdateReviewRequest;
import com.toongather.toongather.domain.review.repository.ReviewKeywordRepository;
import com.toongather.toongather.domain.review.repository.ReviewRecordRepository;
import com.toongather.toongather.domain.review.repository.ReviewRepository;
import com.toongather.toongather.global.common.util.file.File;
import com.toongather.toongather.global.common.util.file.FileFlag;
import com.toongather.toongather.global.common.util.file.FileStore;
import com.toongather.toongather.global.common.util.file.UploadFile;
import com.toongather.toongather.global.common.util.file.service.FileService;
import java.io.IOException;
import java.util.ArrayList;
import javax.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
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
  private ReviewRecordRepository reviewRecordRepository;

  @Autowired
  private ReviewKeywordRepository reviewKeywordRepository;

  @Autowired
  private KeywordRepository keywordRepository;

  @Autowired
  private FileService fileService;

  private final FileStore fileStore;

  /**
   * 리뷰 생성
   */
  public void createReview() {
  }

  /**
   * 리뷰 등록
   */
  @Transactional
  public void saveReview(Review review) {
    reviewRepository.save(review);
  }

  /**
   * 해당 리뷰 찾기
   */
  public ReviewDto findOneReview(String reviewId) {
    return reviewRepository.findReview(reviewId);
  }

  /**
   * 전체 리뷰 조회
   */
  public List<ReviewDto> findAllReview() {

    List<ReviewDto> allReview = reviewRepository.findAll();
    allReview.forEach((v) -> {
      System.out.println("v.getStar() = " + v.getStar());
      System.out.println("v.getRecommendComment() = " + v.getRecommendComment());
      //System.out.println("v.getMember() = " + v.getMember());
      //System.out.println("v.getWebtoon() = " + v.getWebtoon());
    });
    return allReview;
  }

  /**
   * 마이리뷰 나의기록 등록 - 리뷰가 있으면 -> 나의 기록 등록 - 리뷰가 없으면 -> 리뷰 등록 후 나의 기록 등록
   */
  @Transactional
  public ReviewRecordDto createReviewRecord(CreateReviewRecordRequest request) throws IOException {

    System.out.println("request = " + request.getToonId());
    // 나의 기록 있는 리뷰 있는지 웹툰id로 확인
    Review review = reviewRepository.findByToonId(request.getToonId());

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

      List<File> fileList = new ArrayList<>();
      // 파일 정보 저장
      for (UploadFile uploadFile : uploadFiles) {
        fileList.add(File.builder().filePath(uploadFile.getFilePath())
            .uploadFileName(uploadFile.getUploadFileName())
            .storeFileName(uploadFile.getStoreFileName())
            .flag(FileFlag.R).build());
      }
      fileService.saveFiles(fileList);

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
    Review review = reviewRepository.findById(reviewId);
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

    Review review = reviewRepository.findById(request.getReviewId());
    review.setRecommendComment(request.getRecommendComment());
    reviewRepository.save(review);
  }

}
