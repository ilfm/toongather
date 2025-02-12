package com.toongather.toongather.domain.webtoon.domain;

import com.toongather.toongather.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@SequenceGenerator(
        name = "GENRE_KEYWORD_SEQ_GEN",
        sequenceName = "GENRE_KEYWORD_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Entity
@Table(name = "GENRE_KEYWORD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenreKeyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "GENRE_KEYWORD_SEQ_GEN")
    private Long genreKeywordId;

    @Column(nullable = false)
    private String genreKeywordNm;

    @Column(nullable = false)
    private String flag;

    @Builder
    public GenreKeyword(Long genreKeywordId, String genreKeywordNm, String flag) {
        this.genreKeywordId = genreKeywordId;
        this.genreKeywordNm = genreKeywordNm;
        this.flag = flag;
    }

}


