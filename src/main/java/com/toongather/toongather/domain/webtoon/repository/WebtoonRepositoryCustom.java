package com.toongather.toongather.domain.webtoon.repository;

import com.toongather.toongather.domain.webtoon.dto.WebtoonSearchRequest;
import com.toongather.toongather.domain.webtoon.dto.WebtoonSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WebtoonRepositoryCustom {
    Page<WebtoonSearchResponse> searchAll(WebtoonSearchRequest request, Pageable pageable);
}
