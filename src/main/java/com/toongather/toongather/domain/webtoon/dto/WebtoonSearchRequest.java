package com.toongather.toongather.domain.webtoon.dto;

import com.toongather.toongather.domain.webtoon.domain.Age;
import com.toongather.toongather.domain.webtoon.domain.Platform;
import com.toongather.toongather.domain.webtoon.domain.WebtoonStatus;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class WebtoonSearchRequest {
    private String title;
    private String author;
    private Age age;
    private WebtoonStatus status;
    private Platform platform;
    private List<Long> genreKeywordIds;
    private WebtoonSortType sortType;

    @Builder
    public WebtoonSearchRequest(String title, String author, Age age, WebtoonStatus status, Platform platform, List<Long> genreKeywordIds, WebtoonSortType sortType) {
        this.title = title;
        this.author = author;
        this.age = age;
        this.status = status;
        this.platform = platform;
        this.genreKeywordIds = genreKeywordIds;
        this.sortType = sortType;
    }
}
