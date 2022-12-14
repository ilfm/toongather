package com.toongather.toongather.domain.review.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toongather.toongather.SeqGenerator;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.global.common.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "REVIEW")
public class Review extends BaseEntity {

    public Review() {
    }
    @Id
    @GenericGenerator(name="seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
            parameters ={@org.hibernate.annotations.Parameter(name= SeqGenerator.SEQ_NAME,value="REVIEW_SEQ"),
                    @org.hibernate.annotations.Parameter(name= SeqGenerator.PREFIX,value="RV")} )
    @GeneratedValue(generator = "seqGenerator")
    private String reviewId;

    @ManyToOne
    @JoinColumn(name="toonId",foreignKey = @ForeignKey(name = "fk_review_to_toon"))
    private Webtoon webtoon;

    @ManyToOne
    @JoinColumn(name="MEMBER_NO",foreignKey = @ForeignKey(name = "fk_review_to_member"))
    private Member member;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String record;

    @Column(nullable = false)
    private Long star;

    @Column(nullable = false)
    private String amdUserId;


}
