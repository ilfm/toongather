package com.toongather.toongather.domain.webtoon.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toongather.toongather.SeqGenerator;
import com.toongather.toongather.global.common.BaseEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "GENRE_KEYWORD")
public class GenreKeyword extends BaseEntity {

    public GenreKeyword() {
    }

    @Id
    @GenericGenerator(name="seqGenerator", strategy = "com.toongather.toongather.SeqGenerator",
            parameters ={@org.hibernate.annotations.Parameter(name= SeqGenerator.SEQ_NAME,value="GENRE_KEYWORD_SEQ"),
                    @org.hibernate.annotations.Parameter(name= SeqGenerator.PREFIX,value="GK")} )
    @GeneratedValue(generator = "seqGenerator")
    private String genreKeywordId;

    @Column(nullable = false)
    private String genreKeywordNm;

    @Column(nullable = false)
    private String Flag;


    public String getGenreKeywordId() {
        return genreKeywordId;
    }

    public void setGenreKeywordId(String genreKeywordId) {
        this.genreKeywordId = genreKeywordId;
    }

    public String getGenreKeywordNm() {
        return genreKeywordNm;
    }

    public void setGenreKeywordNm(String genreKeywordNm) {
        this.genreKeywordNm = genreKeywordNm;
    }

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

}


