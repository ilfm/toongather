package com.toongather.toongather.domain.webtoon.api;

import com.toongather.toongather.domain.webtoon.dto.*;
import com.toongather.toongather.domain.webtoon.service.WebtoonService;
import com.toongather.toongather.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webtoon")
@RequiredArgsConstructor
public class WebtoonController {

    private final WebtoonService webtoonService;

    @GetMapping("/search")
    public ApiResponse<Page<WebtoonSearchResponse>> searchAllWebtoons(WebtoonSearchRequest request, Pageable pageable) {
        return ApiResponse.ok("/webtoon/search", webtoonService.searchAllWebtoons(request, pageable));
    }

    @GetMapping("/{toonId}")
    public ApiResponse<WebtoonResponse> readWebtoon(@PathVariable Long toonId) {
        return ApiResponse.ok("/webtoon/" + toonId, webtoonService.readWebtoon(toonId));
    }

    // todo: @Valid 추가
    @PostMapping("/new")
    public ResponseEntity<WebtoonCreateResponse> createWebtoon(@RequestBody WebtoonRequest request) {
        return ResponseEntity.ok(webtoonService.createWebtoon(request));
    }

    @PutMapping("/{toonId}")
    public ResponseEntity<Void> updateWebtoon(@PathVariable Long toonId, @RequestBody WebtoonRequest request) {
        webtoonService.updateWebtoon(toonId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{toonId}")
    public ResponseEntity<Void> deleteWebtoon(@PathVariable Long toonId) {
        webtoonService.deleteWebtoon(toonId);
        return ResponseEntity.ok().build();
    }
}
