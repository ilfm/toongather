package com.toongather.toongather.domain.webtoon.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toongather.toongather.SeqGenerator;
import com.toongather.toongather.global.common.BaseEntity;
import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@SequenceGenerator(
    name = "GENRE_KEYWORD_SEQ_GEN",
    sequenceName = "GENRE_KEYWORD_SEQ",
    initialValue = 1,
    allocationSize = 1
)
@Entity
@Table(name = "GENRE_KEYWORD")
public class GenreKeyword extends BaseEntity {

    public GenreKeyword() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
        generator = "GENRE_KEYWORD_SEQ_GEN")
    private Long genreKeywordId;

    @Column(nullable = false)
    private String genreKeywordNm;

    @Column(nullable = false)
    private String flag;

}


