package com.toongather.toongather.domain.review.domain;

import com.toongather.toongather.SeqGenerator;
import com.toongather.toongather.global.common.BaseEntity;
import com.toongather.toongather.global.common.util.file.File;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;


@Getter
@Table(name = "REVIEW_RECORD")
@Entity
public class ReviewRecord extends BaseEntity {

  public ReviewRecord() {

  }

  @Builder
  public ReviewRecord(Review review, String record) {
    this.review = review;
    this.record = record;
  }

  @Id
  @GenericGenerator(name = "seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
      parameters = {
          @org.hibernate.annotations.Parameter(name = SeqGenerator.SEQ_NAME, value = "REVIEW_RECORD_SEQ"),
          @org.hibernate.annotations.Parameter(name = SeqGenerator.PREFIX, value = "RR")})
  @GeneratedValue(generator = "seqGenerator")
  private String reviewRecordId;

  @ManyToOne
  @JoinColumn(name = "reviewId", foreignKey = @ForeignKey(name = "fk_reviewrecord_to_review"))
  private Review review;

  @Column(columnDefinition = "TEXT")
  private String record;

  //@OneToMany(mappedBy = "reviewRecord")
  //private List<File> files = new ArrayList<>();
}
