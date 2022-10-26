package com.toongather.toongather.domain.webtoon.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
@Entity
@Table(name = "WEBTOON_PLATFORM")
public class Platform {

    public Platform() {

    }

    @Id
    private String platformId;

    @Column(nullable = false)
    private String platformNm;

    @Column(nullable = false)
    private String regUserId;

    @Column(nullable = false)
    private String amdUserId;

    @Column(nullable = false)
    private LocalDateTime regDt;

    @Column(nullable = false)
    private LocalDateTime amdDt;

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getPlatformNm() {
        return platformNm;
    }

    public void setPlatformNm(String platformNm) {
        this.platformNm = platformNm;
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
