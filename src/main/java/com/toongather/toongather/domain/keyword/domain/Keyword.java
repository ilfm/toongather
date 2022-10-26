package com.toongather.toongather.domain.keyword.domain;


import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
@Entity
@Table(name = "KEYWORD")
public class Keyword {

    public Keyword() {
    }

    @Id
    private String keywordId;

    @Column(nullable = false)
    private String keywordNm;

    @Column(nullable = false)
    private String regUserId;

    @Column(nullable = false)
    private String amdUserId;

    @Column(nullable = false)
    private LocalDateTime regDt;

    @Column(nullable = false)
    private LocalDateTime amdDt;

    public String getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(String keywordId) {
        this.keywordId = keywordId;
    }

    public String getKeywordNm() {
        return keywordNm;
    }

    public void setKeywordNm(String keywordNm) {
        this.keywordNm = keywordNm;
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

    public LocalDateTime getRegDt() {
        return regDt;
    }

    public void setRegDt(LocalDateTime regDt) {
        this.regDt = regDt;
    }

    public LocalDateTime getAmdDt() {
        return amdDt;
    }

    public void setAmdDt(LocalDateTime amdDt) {
        this.amdDt = amdDt;
    }
}
