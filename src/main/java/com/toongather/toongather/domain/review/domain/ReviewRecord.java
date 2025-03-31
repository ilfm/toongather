package com.toongather.toongather.domain.review.domain;

import com.toongather.toongather.SeqGenerator;
import com.toongather.toongather.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;


@Getter
@SequenceGenerator(
    name = "REVIEW_RECORD_GEN",
    sequenceName = "REVIEW_RECORD_SEQ",
    initialValue = 1,
    allocationSize = 1
)
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
      generator = "REVIEW_RECORD_GEN")
  private Long reviewRecordId;

  @ManyToOne
  @JoinColumn(name = "reviewId", foreignKey = @ForeignKey(name = "fk_reviewrecord_to_review"))
  private Review review;

  @Column(columnDefinition = "TEXT")
  private String record;

  //@OneToMany(mappedBy = "reviewRecord")
  //private List<File> files = new ArrayList<>();

  public void updateReviewRecord(String record) {
    this.record = record;
  }
}
