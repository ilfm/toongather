package com.toongather.toongather.domain.genrekeyword.dto;

import com.toongather.toongather.domain.genrekeyword.domain.GenreKeyword;
import lombok.Builder;
import lombok.Getter;

@Getter
public class GenreKeywordResponse {
    private Long genreKeywordId;
    private String genreKeywordNm;
    private String flag;

    @Builder
    private GenreKeywordResponse(Long genreKeywordId, String genreKeywordNm, String flag) {
        this.genreKeywordId = genreKeywordId;
        this.genreKeywordNm = genreKeywordNm;
        this.flag = flag;
    }

    public static GenreKeywordResponse of(GenreKeyword genreKeyword) {
        return GenreKeywordResponse.builder()
                .genreKeywordId(genreKeyword.getGenreKeywordId())
                .genreKeywordNm(genreKeyword.getGenreKeywordNm())
                .flag(genreKeyword.getFlag())
                .build();
    }
}
