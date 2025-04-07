package com.toongather.toongather.domain.webtoon.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum WebtoonSortType {
    TITLE_ASC("오름차순"),
    TITLE_DESC("내림차순");

    private final String text;
}
