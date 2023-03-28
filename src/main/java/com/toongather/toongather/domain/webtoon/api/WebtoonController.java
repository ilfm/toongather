package com.toongather.toongather.domain.webtoon.api;


import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;
import com.toongather.toongather.domain.webtoon.service.WebtoonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.eclipse.jdt.internal.compiler.ast.IJavadocTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/webtoon")
@RequiredArgsConstructor
public class WebtoonController {

    private final WebtoonRepository webtoonRepository;

    // 웹툰 상세 가져오기
    @GetMapping("/{toonId}")
    public Webtoon getwebToonDetail(@PathVariable String toonId){
        return webtoonRepository.findById(toonId);
    }

}
