package com.toongather.toongather.domain.webtoon.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WebtoonSearchResponse {
    private Long toonId;
    private String title;
    private String imgPath;

    @Builder
    @QueryProjection
    public WebtoonSearchResponse(Long toonId, String title, String imgPath) {
        this.toonId = toonId;
        this.title = title;
        this.imgPath = imgPath;
    }

    public static WebtoonSearchResponse of(Webtoon webtoon) {
        return WebtoonSearchResponse.builder()
                .toonId(webtoon.getToonId())
                .title(webtoon.getTitle())
                .imgPath(webtoon.getImgPath())
                .build();
    }
}
