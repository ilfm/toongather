package com.toongather.toongather.domain.webtoon.dto;

import com.toongather.toongather.domain.webtoon.domain.*;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class WebtoonRequest {

    private String title;
    private String author;
    private Age age;
    private String summary;
    private WebtoonStatus status;
    private String imgPath;
    private Platform platform;
    private List<Long> genreKeywordIds;

    @Builder
    public WebtoonRequest(String title, String author, Age age, String summary, WebtoonStatus status, String imgPath, Platform platform, List<Long> genreKeywordIds) {
        this.title = title;
        this.author = author;
        this.age = age;
        this.summary = summary;
        this.status = status;
        this.imgPath = imgPath;
        this.platform = platform;
        this.genreKeywordIds = genreKeywordIds;
    }

    public Webtoon toEntity(List<GenreKeyword> genreKeywords) {
        return Webtoon.builder()
                .title(title)
                .author(author)
                .age(age)
                .summary(summary)
                .status(status)
                .imgPath(imgPath)
                .platform(platform)
                .genreKeywords(genreKeywords)
                .build();
    }

}
