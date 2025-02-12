package com.toongather.toongather.domain.webtoon.service;


import com.toongather.toongather.domain.webtoon.domain.GenreKeyword;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.dto.WebtoonRequest;
import com.toongather.toongather.domain.webtoon.dto.WebtoonCreateResponse;
import com.toongather.toongather.domain.webtoon.repository.GenreKeywordRepository;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WebtoonService {

    private final GenreKeywordRepository genreKeywordRepository;
    private final WebtoonRepository webtoonRepository;

    @Transactional
    public WebtoonCreateResponse createWebtoon(WebtoonRequest request) {
        List<GenreKeyword> genreKeywords = getGenreKeywords(request);

        Webtoon webtoon = request.toEntity(genreKeywords);
        Webtoon savedWebtoon = webtoonRepository.save(webtoon);

        return WebtoonCreateResponse.builder()
                .id(savedWebtoon.getToonId())
                .title(savedWebtoon.getTitle())
                .build();
    }

    @Transactional
    public void updateWebtoon(Long toonId, WebtoonRequest request) {
        List<GenreKeyword> genreKeywords = getGenreKeywords(request);

        Webtoon webtoon = webtoonRepository.findById(toonId)
                .orElseThrow(() -> new IllegalArgumentException("해당 웹툰이 존재하지 않습니다."));

        webtoon.update(request.getAge(), request.getSummary(), request.getStatus(), genreKeywords);
    }

    @Transactional
    public void deleteWebtoon(Long toonId) {
        Webtoon webtoon = webtoonRepository.findById(toonId)
                .orElseThrow(() -> new IllegalArgumentException("해당 웹툰이 존재하지 않습니다."));

        webtoonRepository.delete(webtoon);
    }

    private List<GenreKeyword> getGenreKeywords(WebtoonRequest request) {
        List<Long> genreKeywordIds = request.getGenreKeywordIds();
        return genreKeywordRepository.findAllByGenreKeywordIdIn(genreKeywordIds);
    }
}
