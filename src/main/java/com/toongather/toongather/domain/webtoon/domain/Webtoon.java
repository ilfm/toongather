package com.toongather.toongather.domain.webtoon.domain;

import com.toongather.toongather.global.common.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@Table(name = "WEBTOON")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SequenceGenerator(
        name = "WEBTOON_SEQ_GEN",
        sequenceName = "WEBTOON_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class Webtoon extends BaseEntity {

    /*
     * 웹툰 api 사용하기로 함
     * */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "WEBTOON_SEQ_GEN")
    @Column(nullable = false)
    private Long toonId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Age age;                // ALL, OVER15, OVER19

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WebtoonStatus status;   // END, STOP, ING

    @Column
    private String imgPath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;      // NAVER, DAUM, LEZHIN, KAKAO

    @OneToMany(mappedBy = "webtoon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WebtoonGenreKeyword> webtoonGenreKeywords = new ArrayList<>();

    @Builder
    public Webtoon(Long toonId, String title, String author, Age age, String summary, WebtoonStatus status, String imgPath, Platform platform, List<GenreKeyword> genreKeywords) {
        this.toonId = toonId;
        this.title = title;
        this.author = author;
        this.age = age;
        this.summary = summary;
        this.status = status;
        this.imgPath = imgPath;
        this.platform = platform;
        this.webtoonGenreKeywords = genreKeywords.stream()
                .map(genreKeyword -> new WebtoonGenreKeyword(this, genreKeyword))
                .collect(Collectors.toList());
    }

    public void update(Age age, String summary, WebtoonStatus status, List<GenreKeyword> genreKeywords) {
        this.age = age;
        this.summary = summary;
        this.status = status;
        updateGenreKeywords(genreKeywords);
    }

    private void updateGenreKeywords(List<GenreKeyword> genreKeywords) {
        if (genreKeywords == null || genreKeywords.isEmpty()) {
            return;
        }

        this.webtoonGenreKeywords.clear();
        genreKeywords.forEach(genreKeyword ->
                this.webtoonGenreKeywords.add(new WebtoonGenreKeyword(this, genreKeyword))
        );
    }

}
