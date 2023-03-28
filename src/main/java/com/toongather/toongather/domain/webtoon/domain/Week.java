package com.toongather.toongather.domain.webtoon.domain;


import com.toongather.toongather.SeqGenerator;
import com.toongather.toongather.global.common.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;


@Getter
@Entity
@Table(name = "WEEK")
public class Week extends BaseEntity {


    @Id
    @GenericGenerator(name="seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
            parameters ={@org.hibernate.annotations.Parameter(name= SeqGenerator.SEQ_NAME,value="WEEK_SEQ"),
                    @org.hibernate.annotations.Parameter(name= SeqGenerator.PREFIX,value="WW")} )
    @GeneratedValue(generator = "seqGenerator")
    private String weekId;

    @Column(nullable = false)
    private String weekNm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="toonId",foreignKey = @ForeignKey(name = "fk_week_to_toon"))
    private Webtoon webtoon;

    @Column(nullable = false)
    private String regUserId;

    @Column(nullable = false)
    private String amdUserId;




}
