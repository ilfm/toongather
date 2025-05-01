package com.toongather.toongather.domain.webtoon.domain;

import com.toongather.toongather.domain.genrekeyword.domain.GenreKeyword;
import com.toongather.toongather.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Getter
@SequenceGenerator(
        name = "WEBTOON_GENRE_KEYWORD_GEN",
        sequenceName = "WEBTOON_GENRE_KEYWORD_SEQ",
        initialValue = 1,
        allocationSize = 1
)
@Table(name = "WEBTOON_GENRE_KEYWORD")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebtoonGenreKeyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "WEBTOON_GENRE_KEYWORD_GEN")
    private Long webtoonGenreKeywordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toonId", foreignKey = @ForeignKey(name = "fk_toongenrekeword_to_toon"))
    private Webtoon webtoon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genreKeywordId", foreignKey = @ForeignKey(name = "fk_toongenrekeword_to_genrekeyword"))
    private GenreKeyword genreKeyword;

    @Builder
    public WebtoonGenreKeyword(Webtoon webtoon, GenreKeyword genreKeyword) {
        this.webtoon = webtoon;
        this.genreKeyword = genreKeyword;
    }

}
