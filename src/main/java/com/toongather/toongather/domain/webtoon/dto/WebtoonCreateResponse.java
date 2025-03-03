package com.toongather.toongather.domain.webtoon.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WebtoonCreateResponse {

    private Long id;
    private String title;

    @Builder
    public WebtoonCreateResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }

}
