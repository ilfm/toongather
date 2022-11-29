package com.toongather.toongather.domain.review.domain;


import com.toongather.toongather.SeqGenerator;
import com.toongather.toongather.domain.member.domain.Member;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.global.common.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Table(name="REVIEW_KEYWORD")
@Entity
public class ReviewKeyword extends BaseEntity {

    @Id
    @GenericGenerator(name="seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
            parameters ={@org.hibernate.annotations.Parameter(name= SeqGenerator.SEQ_NAME,value="REVIEW_KEYWORD_SEQ"),
                    @org.hibernate.annotations.Parameter(name= SeqGenerator.PREFIX,value="RK")} )
    @GeneratedValue(generator = "seqGenerator")
    private String reviewKeywordId;

    @ManyToOne
    @JoinColumn(name="reviewId",foreignKey = @ForeignKey(name = "fk_reviewkeword_to_review"))
    private Review review;

    @ManyToOne
    @JoinColumn(name="toonId",foreignKey = @ForeignKey(name = "fk_reviewkeword_to_toon"))
    private Webtoon webtoon;

    @ManyToOne
    @JoinColumn(name="MEMBER_NO",foreignKey = @ForeignKey(name = "fk_reviewkeword_to_member"))
    private Member member;

    @Column
    private String keywordNm;

    public String getReviewKeywordId() {
        return reviewKeywordId;
    }


}
