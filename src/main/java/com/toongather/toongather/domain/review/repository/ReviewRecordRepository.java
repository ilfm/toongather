package com.toongather.toongather.domain.review.repository;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toongather.toongather.domain.review.domain.QReviewRecord;
import com.toongather.toongather.domain.review.domain.ReviewRecord;
import com.toongather.toongather.domain.review.dto.ReviewRecordDto;
import java.util.List;
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

  private final JPAQueryFactory jpaQueryFactory;

  public ReviewRecordRepository(EntityManager em) {
    this.em = em;
    this.jpaQueryFactory = new JPAQueryFactory(em);
  }

  // 나의 기록 review reocrd 저장
  public String saveReviewRecord(ReviewRecord reviewRecord) {
    if (reviewRecord.getReviewRecordId() == null) {
      em.persist(reviewRecord);
      em.flush();
    } else {
      // todo merge 문 쓰면 안됨 변경감지로 변경
      em.merge(reviewRecord);
    }
    return reviewRecord.getReviewRecordId();
  }

  public ReviewRecord findById(String reviewRecordId){return em.find(ReviewRecord.class, reviewRecordId);}

  /*
  * 나의 기록 조회
  * */
  public List<ReviewRecord> selectReviewRecordList(String reviewId) {
    QReviewRecord rr = new QReviewRecord("rr");
    List<ReviewRecord> reviewRecordsList = jpaQueryFactory.select(rr).from(rr)
        .where(rr.review.reviewId.eq(reviewId)).fetch();
    return reviewRecordsList;
  }
}
