package com.toongather.toongather.domain.webtoon.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toongather.toongather.SeqGenerator;
import com.toongather.toongather.global.common.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
@Entity
@Table(name="WEBTOON")
public class Webtoon extends BaseEntity {

    public Webtoon() {

    }
    @Id
    @GenericGenerator(name="seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
            parameters ={@org.hibernate.annotations.Parameter(name= SeqGenerator.SEQ_NAME,value="WEBTOON_SEQ"),
                    @org.hibernate.annotations.Parameter(name= SeqGenerator.PREFIX,value="WT")} )
    @GeneratedValue(generator = "seqGenerator")
    private String toonId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String summary;

    @Column(nullable = false)
    private String writerNm;

    @Column(nullable = false)
    private String age;

    @Column(nullable = false)
    private String endFlag;

    @Column(nullable = false)
    private String amdUserId;


    public String getToonId() {
        return toonId;
    }

    public void setToonId(String toonId) {
        this.toonId = toonId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getWriterNm() {
        return writerNm;
    }

    public void setWriterNm(String writerNm) {
        this.writerNm = writerNm;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEndFlag() {
        return endFlag;
    }

    public void setEndFlag(String endFlag) {
        this.endFlag = endFlag;
    }

    public String getAmdUserId() {
        return amdUserId;
    }

    public void setAmdUserId(String amdUserId) {
        this.amdUserId = amdUserId;
    }

}
