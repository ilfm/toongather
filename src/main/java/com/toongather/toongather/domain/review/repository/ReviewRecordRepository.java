package com.toongather.toongather.domain.review.repository;


import com.toongather.toongather.domain.review.domain.ReviewRecord;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class ReviewRecordRepository {

  @PersistenceContext
  private EntityManager em;


  // 나의 기록 review reocrd 저장
  public void saveReviewRecord(ReviewRecord reviewRecord) {
    if (reviewRecord.getReviewRecordId() == null) {
      em.persist(reviewRecord);
    } else {
      // todo merge 문 쓰면 안됨 변경감지로 변경
      em.merge(reviewRecord);
    }
  }
}
