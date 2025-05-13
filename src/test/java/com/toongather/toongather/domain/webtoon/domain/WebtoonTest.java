package com.toongather.toongather.domain.webtoon.domain;

import com.toongather.toongather.domain.genrekeyword.domain.GenreKeyword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static com.toongather.toongather.domain.webtoon.domain.Age.ALL;
import static com.toongather.toongather.domain.webtoon.domain.Age.OVER15;
import static com.toongather.toongather.domain.webtoon.domain.Platform.NAVER;
import static com.toongather.toongather.domain.webtoon.domain.WebtoonStatus.END;
import static com.toongather.toongather.domain.webtoon.domain.WebtoonStatus.ING;
import static org.assertj.core.api.Assertions.assertThat;

class WebtoonTest {

    @DisplayName("웹툰 생성 시 장르 키워드가 정상적으로 설정된다")
    @Test
    void createWebtoonWithGenreKeywords() {
        // given
        List<GenreKeyword> genreKeywords = List.of(
                createGenreKeyword(1L, "로맨스", "Y"),
                createGenreKeyword(2L, "판타지", "Y")
        );

        // when
        Webtoon webtoon = createWebtoon("테스트 웹툰", "테스트 작가", ALL, ING, NAVER, "테스트 줄거리", genreKeywords);

        // then
        assertThat(webtoon.getTitle()).isEqualTo("테스트 웹툰");
        assertThat(webtoon.getAuthor()).isEqualTo("테스트 작가");
        assertThat(webtoon.getWebtoonGenreKeywords()).hasSize(2);

        List<String> keywordNames = webtoon.getWebtoonGenreKeywords().stream()
                .map(wgk -> wgk.getGenreKeyword().getGenreKeywordNm())
                .collect(Collectors.toList());

        assertThat(keywordNames).containsExactlyInAnyOrder("로맨스", "판타지");

        // 양방향 연관관계 매핑 확인
        webtoon.getWebtoonGenreKeywords().forEach(wgk ->
                assertThat(wgk.getWebtoon()).isSameAs(webtoon)
        );
    }

    @DisplayName("웹툰 정보 업데이트 시 장르 키워드가 정상적으로 변경된다")
    @Test
    void updateWebtoonWithNewGenreKeywords() {
        // given
        List<GenreKeyword> originalKeywords = List.of(
                createGenreKeyword(1L, "로맨스", "Y"),
                createGenreKeyword(2L, "판타지", "Y")
        );
        Webtoon webtoon = createWebtoon("원본 웹툰", "원본 작가", ALL, ING, NAVER, "원본 줄거리", originalKeywords);


        List<GenreKeyword> newKeywords = List.of(
                createGenreKeyword(3L, "액션", "Y"),
                createGenreKeyword(4L, "스릴러", "Y"),
                createGenreKeyword(5L, "코미디", "Y")
        );

        // when
        webtoon.update(OVER15, "수정된 줄거리", END, newKeywords);

        // then
        assertThat(webtoon.getAge()).isEqualTo(OVER15);
        assertThat(webtoon.getSummary()).isEqualTo("수정된 줄거리");
        assertThat(webtoon.getStatus()).isEqualTo(END);
        assertThat(webtoon.getWebtoonGenreKeywords()).hasSize(3);

        List<String> updatedKeywordNames = webtoon.getWebtoonGenreKeywords().stream()
                .map(wgk -> wgk.getGenreKeyword().getGenreKeywordNm())
                .collect(Collectors.toList());

        assertThat(updatedKeywordNames).containsExactlyInAnyOrder("액션", "스릴러", "코미디");
        assertThat(updatedKeywordNames).doesNotContain("로맨스", "판타지");
    }

    private Webtoon createWebtoon(String title, String author, Age age, WebtoonStatus status,
                                  Platform platform, String summary, List<GenreKeyword> genreKeywords) {
        return Webtoon.builder()
                .title(title)
                .author(author)
                .age(age)
                .status(status)
                .platform(platform)
                .summary(summary)
                .genreKeywords(genreKeywords)
                .build();
    }

    private GenreKeyword createGenreKeyword(Long id, String name, String flag) {
        return GenreKeyword.builder()
                .genreKeywordId(id)
                .genreKeywordNm(name)
                .flag(flag)
                .build();
    }

}