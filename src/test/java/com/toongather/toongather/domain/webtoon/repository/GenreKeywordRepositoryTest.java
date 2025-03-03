package com.toongather.toongather.domain.webtoon.repository;

import com.toongather.toongather.domain.webtoon.domain.GenreKeyword;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class GenreKeywordRepositoryTest {

    @Autowired
    private GenreKeywordRepository genreKeywordRepository;

    @DisplayName("아이디로 장르 키워드들을 조회한다.")
    @Test
    void findAllByGenreKeywordIdIn() {
        // given
        GenreKeyword genreKeyword1 = createGenreKeyword("로맨스", "Y");
        GenreKeyword genreKeyword2 = createGenreKeyword("판타지", "Y");
        GenreKeyword genreKeyword3 = createGenreKeyword("스릴러", "Y");
        List<GenreKeyword> savedGenreKeywords = genreKeywordRepository.saveAll(List.of(genreKeyword1, genreKeyword2, genreKeyword3));

        // when
        List<GenreKeyword> genreKeywords = genreKeywordRepository.findAllByGenreKeywordIdIn(savedGenreKeywords.stream()
                .map(GenreKeyword::getGenreKeywordId)
                .toList());

        // then
        assertThat(genreKeywords).hasSize(3)
                .extracting("genreKeywordNm", "flag")
                .containsExactlyInAnyOrder(
                        tuple("로맨스", "Y"),
                        tuple("판타지", "Y"),
                        tuple("스릴러", "Y")
                );
    }

    private GenreKeyword createGenreKeyword(String genreKeywordNm, String flag) {
        return GenreKeyword.builder()
                .genreKeywordNm(genreKeywordNm)
                .flag(flag)
                .build();
    }

}