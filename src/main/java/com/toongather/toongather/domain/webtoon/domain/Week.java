package com.toongather.toongather.domain.webtoon.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toongather.toongather.global.common.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
@Entity
@Table(name = "WEBTOON_WEEK")
public class Week extends BaseEntity {

    @Id
    private String weekId;

    @Column(nullable = false)
    private String weekNm;

    @ManyToOne
    @JoinColumn(name="toonId")
    private Webtoon webtoon;

    @Column(nullable = false)
    private String regUserId;

    @Column(nullable = false)
    private String amdUserId;

    @Column(nullable = false)
    private LocalDateTime regDt;

    @Column(nullable = false)
    private LocalDateTime amdDt;

    public String getWeekId() {
        return weekId;
    }

    public void setWeekId(String weekId) {
        this.weekId = weekId;
    }

    public String getWeekNm() {
        return weekNm;
    }

    public void setWeekNm(String weekNm) {
        this.weekNm = weekNm;
    }

    public String getRegUserId() {
        return regUserId;
    }

    public void setRegUserId(String regUserId) {
        this.regUserId = regUserId;
    }

    public String getAmdUserId() {
        return amdUserId;
    }

    public void setAmdUserId(String amdUserId) {
        this.amdUserId = amdUserId;
    }

    public void setRegDt(LocalDateTime regDt) {
        this.regDt = regDt;
    }

    public void setAmdDt(LocalDateTime amdDt) {
        this.amdDt = amdDt;
    }


}
