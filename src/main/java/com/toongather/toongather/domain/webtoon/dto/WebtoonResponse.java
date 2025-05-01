package com.toongather.toongather.domain.webtoon.dto;

import com.toongather.toongather.domain.genrekeyword.dto.GenreKeywordResponse;
import com.toongather.toongather.domain.webtoon.domain.Age;
import com.toongather.toongather.domain.webtoon.domain.Platform;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.domain.WebtoonStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class WebtoonResponse {
    private Long toonId;
    private String title;
    private String author;
    private Age age;
    private String summary;
    private WebtoonStatus status;
    private String imgPath;
    private Platform platform;
    private List<GenreKeywordResponse> genreKeywords;

    @Builder
    public WebtoonResponse(Long toonId, String title, String author, Age age, String summary,
                           WebtoonStatus status, String imgPath, Platform platform,
                           List<GenreKeywordResponse> genreKeywords) {
        this.toonId = toonId;
        this.title = title;
        this.author = author;
        this.age = age;
        this.summary = summary;
        this.status = status;
        this.imgPath = imgPath;
        this.platform = platform;
        this.genreKeywords = genreKeywords;
    }

    public static WebtoonResponse from(Webtoon webtoon) {
        return WebtoonResponse.builder()
                .toonId(webtoon.getToonId())
                .title(webtoon.getTitle())
                .author(webtoon.getAuthor())
                .age(webtoon.getAge())
                .summary(webtoon.getSummary())
                .status(webtoon.getStatus())
                .imgPath(webtoon.getImgPath())
                .platform(webtoon.getPlatform())
                .genreKeywords(webtoon.getWebtoonGenreKeywords().stream()
                        .map(webtoonGenreKeyword -> GenreKeywordResponse.of(webtoonGenreKeyword.getGenreKeyword()))
                        .toList())
                .build();
    }
}
