package com.toongather.toongather.domain.webtoon.repository;

import com.toongather.toongather.domain.genrekeyword.domain.GenreKeyword;
import com.toongather.toongather.domain.genrekeyword.repository.GenreKeywordRepository;
import com.toongather.toongather.domain.webtoon.domain.*;
import com.toongather.toongather.domain.webtoon.dto.WebtoonSearchRequest;
import com.toongather.toongather.domain.webtoon.dto.WebtoonSearchResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.toongather.toongather.domain.webtoon.domain.Age.ALL;
import static com.toongather.toongather.domain.webtoon.domain.Age.OVER15;
import static com.toongather.toongather.domain.webtoon.domain.Platform.NAVER;
import static com.toongather.toongather.domain.webtoon.domain.WebtoonStatus.END;
import static com.toongather.toongather.domain.webtoon.domain.WebtoonStatus.ING;
import static com.toongather.toongather.domain.webtoon.dto.WebtoonSortType.TITLE_ASC;
import static com.toongather.toongather.domain.webtoon.dto.WebtoonSortType.TITLE_DESC;
import static org.junit.jupiter.api.Assertions.*;

class WebtoonRepositoryTest extends RepositoryTestSupport {

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Autowired
    private GenreKeywordRepository genreKeywordRepository;

    // 검색 조건 테스트를 위한 파라미터 소스
    static Stream<Arguments> searchConditionsProvider() {
        Stream<Arguments> argumentsStream = Stream.of(
                // 테스트 케이스명, 검색 요청 객체, 예상 결과 수, 예상 첫 번째 결과 제목
                Arguments.of("제목으로 검색",
                        WebtoonSearchRequest.builder().title("치즈인더트랩").sortType(TITLE_ASC).build(),
                        1, List.of("치즈인더트랩")),
                Arguments.of("작가로 검색",
                        WebtoonSearchRequest.builder().author("순끼").sortType(TITLE_ASC).build(),
                        2, List.of("세기말 풋사과 보습학원", "치즈인더트랩")),
                Arguments.of("연령으로 검색",
                        WebtoonSearchRequest.builder().age(OVER15).sortType(TITLE_ASC).build(),
                        1, List.of("치즈인더트랩")),
                Arguments.of("플랫폼으로 검색",
                        WebtoonSearchRequest.builder().platform(NAVER).sortType(TITLE_ASC).build(),
                        2, List.of("세기말 풋사과 보습학원", "치즈인더트랩")),
                Arguments.of("상태로 검색",
                        WebtoonSearchRequest.builder().status(END).sortType(TITLE_ASC).build(),
                        1, List.of("치즈인더트랩"))
        );

        return argumentsStream;
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("searchConditionsProvider")
    void searchAllByDynamicConditions(String testName,
                                      WebtoonSearchRequest request,
                                      int expectedSize,
                                      List<String> expectedTitles) {
        // given
        GenreKeyword genreKeyword1 = createGenreKeyword("일상", "Y");
        GenreKeyword genreKeyword2 = createGenreKeyword("드라마", "Y");
        GenreKeyword genreKeyword3 = createGenreKeyword("액션", "Y");
        genreKeywordRepository.saveAll(List.of(genreKeyword1, genreKeyword2, genreKeyword3));

        Webtoon webtoon1 = createWebtoon("치즈인더트랩", "순끼", OVER15, END, NAVER,
                List.of(genreKeyword1, genreKeyword2));

        Webtoon webtoon2 = createWebtoon("세기말 풋사과 보습학원", "순끼", ALL, ING, NAVER,
                List.of(genreKeyword3));
        webtoonRepository.saveAll(List.of(webtoon1, webtoon2));

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<WebtoonSearchResponse> result = webtoonRepository.searchAll(request, pageable);

        // then
        assertEquals(expectedSize, result.getTotalElements());
        if (expectedSize > 0) {
            // 결과 리스트에서 제목만 추출
            List<String> actualTitles = result.getContent().stream()
                    .map(WebtoonSearchResponse::getTitle)
                    .collect(Collectors.toList());

            // 예상 제목 리스트와 실제 제목 리스트 비교
            assertEquals(expectedTitles, actualTitles);
        }
    }

    @DisplayName("장르 키워드로 웹툰을 조회한다.")
    @Test
    void searchAllByGenreKeywords() {
        // given
        GenreKeyword genreKeyword1 = createGenreKeyword("일상", "Y");
        GenreKeyword genreKeyword2 = createGenreKeyword("드라마", "Y");
        GenreKeyword genreKeyword3 = createGenreKeyword("액션", "Y");
        genreKeywordRepository.saveAll(List.of(genreKeyword1, genreKeyword2, genreKeyword3));

        Webtoon webtoon1 = createWebtoon("치즈인더트랩", "순끼", OVER15, END, NAVER,
                List.of(genreKeyword1, genreKeyword2));

        Webtoon webtoon2 = createWebtoon("세기말 풋사과 보습학원", "순끼", ALL, ING, NAVER,
                List.of(genreKeyword3));
        webtoonRepository.saveAll(List.of(webtoon1, webtoon2));

        WebtoonSearchRequest request = WebtoonSearchRequest
                .builder()
                .genreKeywordIds(List.of(genreKeyword1.getGenreKeywordId(), genreKeyword2.getGenreKeywordId()))
                .sortType(TITLE_ASC)
                .build();

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<WebtoonSearchResponse> result = webtoonRepository.searchAll(request, pageable);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals("치즈인더트랩", result.getContent().get(0).getTitle());
    }

    static Stream<Arguments> sortConditionProvider() {
        return Stream.of(
                Arguments.of("제목 오름차순",
                        WebtoonSearchRequest.builder().sortType(TITLE_ASC).build(),
                        List.of("세기말 풋사과 보습학원", "치즈인더트랩")),
                Arguments.of("제목 내림차순",
                        WebtoonSearchRequest.builder().sortType(TITLE_DESC).build(),
                        List.of("치즈인더트랩", "세기말 풋사과 보습학원"))
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("sortConditionProvider")
    void searchAllBySortConditions(String testName,
                                   WebtoonSearchRequest request,
                                   List<String> expectedTitles) {
        // given
        GenreKeyword genreKeyword1 = createGenreKeyword("일상", "Y");
        GenreKeyword genreKeyword2 = createGenreKeyword("드라마", "Y");
        genreKeywordRepository.saveAll(List.of(genreKeyword1, genreKeyword2));

        Webtoon webtoon1 = createWebtoon("치즈인더트랩", "순끼", OVER15, END, NAVER,
                List.of(genreKeyword1, genreKeyword2));

        Webtoon webtoon2 = createWebtoon("세기말 풋사과 보습학원", "순끼", ALL, ING, NAVER,
                List.of(genreKeyword1));
        webtoonRepository.saveAll(List.of(webtoon1, webtoon2));

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<WebtoonSearchResponse> result = webtoonRepository.searchAll(request, pageable);

        // then
        assertEquals(expectedTitles.size(), result.getTotalElements());
        if (!expectedTitles.isEmpty()) {
            List<String> actualTitles = result.getContent().stream()
                    .map(WebtoonSearchResponse::getTitle)
                    .collect(Collectors.toList());

            assertEquals(expectedTitles, actualTitles);
        }
    }

    @DisplayName("웹툰을 페이지 단위로 조회한다.")
    @Test
    void searchAllWithPaging() {
        // given
        GenreKeyword genreKeyword1 = createGenreKeyword("일상", "Y");
        GenreKeyword genreKeyword2 = createGenreKeyword("드라마", "Y");
        genreKeywordRepository.saveAll(List.of(genreKeyword1, genreKeyword2));

        Webtoon webtoon1 = createWebtoon("치즈인더트랩", "순끼", OVER15, END, NAVER,
                List.of(genreKeyword1, genreKeyword2));

        Webtoon webtoon2 = createWebtoon("세기말 풋사과 보습학원", "순끼", ALL, ING, NAVER,
                List.of(genreKeyword1));
        webtoonRepository.saveAll(List.of(webtoon1, webtoon2));

        WebtoonSearchRequest request = WebtoonSearchRequest
                .builder()
                .sortType(TITLE_ASC)
                .build();

        Pageable pageable = PageRequest.of(0, 1);

        // when
        Page<WebtoonSearchResponse> result = webtoonRepository.searchAll(request, pageable);

        // then
        assertEquals(2, result.getTotalElements()); // 전체 웹툰 수
        assertEquals(1, result.getContent().size()); // 현재 페이지의 웹툰 수
        assertEquals(2, result.getTotalPages()); // 전체 페이지 수
        assertEquals("세기말 풋사과 보습학원", result.getContent().get(0).getTitle());

        // 두 번째 페이지 조회
        pageable = PageRequest.of(1, 1);
        result = webtoonRepository.searchAll(request, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("치즈인더트랩", result.getContent().get(0).getTitle());
    }

    private Webtoon createWebtoon(String title, String author, Age age, WebtoonStatus status,
                                  Platform platform, List<GenreKeyword> genreKeywords) {
        return Webtoon.builder()
                .title(title)
                .author(author)
                .age(age)
                .status(status)
                .platform(platform)
                .genreKeywords(genreKeywords)
                .build();
    }

    private GenreKeyword createGenreKeyword(String genreKeywordNm, String flag) {
        return GenreKeyword.builder()
                .genreKeywordNm(genreKeywordNm)
                .flag(flag)
                .build();
    }
}