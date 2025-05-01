package com.toongather.toongather.domain.webtoon.service;

import com.toongather.toongather.domain.genrekeyword.domain.GenreKeyword;
import com.toongather.toongather.domain.webtoon.domain.*;
import com.toongather.toongather.domain.webtoon.dto.WebtoonCreateResponse;
import com.toongather.toongather.domain.webtoon.dto.WebtoonRequest;
import com.toongather.toongather.domain.genrekeyword.repository.GenreKeywordRepository;
import com.toongather.toongather.domain.webtoon.dto.WebtoonResponse;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;
import com.toongather.toongather.global.common.error.custom.WebtoonException.WebtoonNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.toongather.toongather.domain.webtoon.domain.Age.*;
import static com.toongather.toongather.domain.webtoon.domain.Platform.*;
import static com.toongather.toongather.domain.webtoon.domain.WebtoonStatus.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebtoonServiceTest {

    @InjectMocks
    private WebtoonService webtoonService;

    @Mock
    private WebtoonRepository webtoonRepository;

    @Mock
    private GenreKeywordRepository genreKeywordRepository;

    @DisplayName("웹툰 상세 조회에 성공한다.")
    @Test
    void readWebtoonSuccess() {
        // given
        Long toonId = 1L;
        Webtoon webtoon = Webtoon.builder()
                .toonId(toonId)
                .title("테스트 웹툰")
                .genreKeywords(List.of(
                        createGenreKeyword(1L, "로맨스판타지", "Y")
                ))
                .build();

        given(webtoonRepository.findById(toonId)).willReturn(Optional.of(webtoon));

        // when
        WebtoonResponse response = webtoonService.readWebtoon(toonId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getToonId()).isEqualTo(toonId);
        assertThat(response.getTitle()).isEqualTo("테스트 웹툰");
    }

    @DisplayName("존재하지 않는 웹툰을 조회하려는 경우 예외가 발생한다.")
    @Test
    void readNonExistentWebtoon() {
        // given
        Long toonId = 999L;
        given(webtoonRepository.findById(toonId)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> webtoonService.readWebtoon(toonId))
                .isInstanceOf(WebtoonNotFoundException.class)
                .hasMessage("해당 웹툰이 존재하지 않습니다.");
    }

    @DisplayName("웹툰 등록에 성공한다.")
    @Test
    void createWebtoonSuccess() {
        // given
        List<Long> genreKeywordIds = List.of(1L, 2L);
        List<GenreKeyword> genreKeywords = List.of(
                createGenreKeyword(1L, "로맨스판타지", "Y"),
                createGenreKeyword(2L, "드라마", "Y")
        );

        WebtoonRequest request = createWebtoonRequest(
                "테스트웹툰", "홍길동", ALL, ING, LEZHIN, "테스트 줄거리", genreKeywordIds);

        // 시퀀스 ID를 시뮬레이션하기 위한 설정
        when(webtoonRepository.save(any(Webtoon.class)))
                .thenAnswer(invocation -> {
                    Webtoon webtoonToSave = invocation.getArgument(0);
                    // 저장 시 ID 생성을 시뮬레이션
                    return Webtoon.builder()
                            .toonId(100L) // 가상의 시퀀스 값
                            .title(webtoonToSave.getTitle())
                            .author(webtoonToSave.getAuthor())
                            .age(webtoonToSave.getAge())
                            .status(webtoonToSave.getStatus())
                            .platform(webtoonToSave.getPlatform())
                            .genreKeywords(genreKeywords)
                            .build();
                });

        when(genreKeywordRepository.findAllByGenreKeywordIdIn(genreKeywordIds)).thenReturn(genreKeywords);

        // when
        WebtoonCreateResponse response = webtoonService.createWebtoon(request);

        // then
        assertThat(response.getId()).isNotNull();

        verify(genreKeywordRepository).findAllByGenreKeywordIdIn(genreKeywordIds);
        verify(webtoonRepository).save(argThat(webtoon -> {
            // 기본 필드 검증
            boolean basicFieldsValid = webtoon.getTitle().equals("테스트웹툰") &&
                    webtoon.getAuthor().equals("홍길동") &&
                    webtoon.getAge() == ALL &&
                    webtoon.getStatus() == ING &&
                    webtoon.getPlatform() == LEZHIN;

            // 장르 관련 검증
            boolean genresValid = webtoon.getWebtoonGenreKeywords().size() == 2 &&
                    webtoon.getWebtoonGenreKeywords().stream()
                            .map(WebtoonGenreKeyword::getGenreKeyword)
                            .map(GenreKeyword::getGenreKeywordNm)
                            .collect(Collectors.toSet())
                            .containsAll(Set.of("로맨스판타지", "드라마"));

            return basicFieldsValid && genresValid;
        }));
    }

    @DisplayName("웹툰을 수정한다.")
    @Test
    void updateWebtoonSuccess() {
        // given
        Long toonId = 100L;
        List<Long> genreKeywordIds = List.of(1L, 2L);
        List<GenreKeyword> genreKeywords = List.of(
                createGenreKeyword(1L, "로맨스판타지", "Y"),
                createGenreKeyword(2L, "드라마", "Y")
        );

        // 원래 웹툰 정보
        Webtoon existingWebtoon = createWebtoon("기존웹툰", "김작가", ALL, ING, LEZHIN, "기존 줄거리",
                List.of(
                        createGenreKeyword(1L, "로맨스판타지", "Y"),
                        createGenreKeyword(2L, "드라마", "Y")
                ));

        // 업데이트 요청 객체
        WebtoonRequest request = createWebtoonRequest(
                "기존웹툰", "김작가", OVER15, END, LEZHIN, "업데이트된 줄거리", genreKeywordIds);

        when(webtoonRepository.findById(toonId)).thenReturn(Optional.of(existingWebtoon));
        when(genreKeywordRepository.findAllByGenreKeywordIdIn(genreKeywordIds)).thenReturn(genreKeywords);

        // when
        webtoonService.updateWebtoon(toonId, request);

        // then
        verify(webtoonRepository).findById(toonId);
        verify(genreKeywordRepository).findAllByGenreKeywordIdIn(genreKeywordIds);

        // 웹툰 필드 검증
        assertThat(existingWebtoon.getAge()).isEqualTo(OVER15);
        assertThat(existingWebtoon.getStatus()).isEqualTo(END);
        assertThat(existingWebtoon.getSummary()).isEqualTo("업데이트된 줄거리");
    }

    @DisplayName("존재하지 않는 웹툰을 수정하려는 경우 예외가 발생한다.")
    @Test
    void updateNonExistentWebtoon() {
        // given
        Long nonExistentToonId = 999L;
        List<Long> genreKeywordIds = List.of(3L, 4L);

        WebtoonRequest request = createWebtoonRequest(
                "존재하지 않는 웹툰", "미상", OVER15, END, LEZHIN, "존재하지 않는 웹툰 줄거리", genreKeywordIds);

        when(webtoonRepository.findById(nonExistentToonId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> webtoonService.updateWebtoon(nonExistentToonId, request))
                .isInstanceOf(WebtoonNotFoundException.class)
                .hasMessage("해당 웹툰이 존재하지 않습니다.");

        verify(genreKeywordRepository, never()).findAllByGenreKeywordIdIn(any());
    }

    @DisplayName("웹툰을 삭제한다.")
    @Test
    void deleteWebtoonSuccess() {
        // given
        Long toonId = 100L;

        Webtoon existingWebtoon = createWebtoon("기존웹툰", "김작가", ALL, ING, LEZHIN, "기존 줄거리",
                List.of(
                        createGenreKeyword(1L, "로맨스판타지", "Y"),
                        createGenreKeyword(2L, "드라마", "Y")
                ));

        when(webtoonRepository.findById(toonId)).thenReturn(Optional.of(existingWebtoon));

        // when
        webtoonService.deleteWebtoon(toonId);

        // then
        verify(webtoonRepository).findById(toonId);
        verify(webtoonRepository).delete(existingWebtoon);
    }

    @DisplayName("존재하지 않는 웹툰을 삭제하려는 경우 예외가 발생한다.")
    @Test
    void deleteNonExistentWebtoon() {
        // given
        Long nonExistentToonId = 999L;
        when(webtoonRepository.findById(nonExistentToonId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> webtoonService.deleteWebtoon(nonExistentToonId))
                .isInstanceOf(WebtoonNotFoundException.class)
                .hasMessage("해당 웹툰이 존재하지 않습니다.");

        verify(webtoonRepository).findById(nonExistentToonId);
        verify(webtoonRepository, never()).delete(any());
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

    private WebtoonRequest createWebtoonRequest(
            String title, String author, Age age, WebtoonStatus status,
            Platform platform, String summary, List<Long> genreKeywordIds) {
        return WebtoonRequest.builder()
                .title(title)
                .author(author)
                .age(age)
                .status(status)
                .platform(platform)
                .summary(summary)
                .genreKeywordIds(genreKeywordIds)
                .build();
    }

    private GenreKeyword createGenreKeyword(Long genreKeywordId, String genreKeywordNm, String flag) {
        return GenreKeyword.builder()
                .genreKeywordId(genreKeywordId)
                .genreKeywordNm(genreKeywordNm)
                .flag(flag)
                .build();
    }

}