package com.toongather.toongather.domain.webtoon.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toongather.toongather.domain.webtoon.dto.WebtoonSearchRequest;
import com.toongather.toongather.domain.webtoon.dto.WebtoonSortType;
import com.toongather.toongather.domain.webtoon.dto.QWebtoonSearchResponse;
import com.toongather.toongather.domain.webtoon.dto.WebtoonSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.toongather.toongather.domain.webtoon.domain.QWebtoon.webtoon;
import static com.toongather.toongather.domain.webtoon.domain.QWebtoonGenreKeyword.webtoonGenreKeyword;

@RequiredArgsConstructor
public class WebtoonRepositoryImpl implements WebtoonRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<WebtoonSearchResponse> searchAll(WebtoonSearchRequest request, Pageable pageable) {

        List<WebtoonSearchResponse> webtoons = queryFactory
                .select(new QWebtoonSearchResponse(
                        webtoon.toonId,
                        webtoon.title,
                        webtoon.imgPath
                ))
                .from(webtoon)
                .leftJoin(webtoon.webtoonGenreKeywords, webtoonGenreKeyword)
                .where(buildConditions(request))
                .groupBy(webtoon.toonId)
                .orderBy(createOrderSpecifier(request.getSortType()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(webtoon.toonId.countDistinct())
                .from(webtoon)
                .leftJoin(webtoon.webtoonGenreKeywords, webtoonGenreKeyword)
                .where(buildConditions(request))
                .fetchOne();

        return new PageImpl<>(webtoons, pageable, total);
    }

    private BooleanBuilder buildConditions(WebtoonSearchRequest request) {
        BooleanBuilder builder = new BooleanBuilder();

        // (title contains OR author contains)
        BooleanBuilder searchBuilder = new BooleanBuilder();
        searchBuilder.or(containsIfNotEmpty(webtoon.title, request.getTitle()));
        searchBuilder.or(containsIfNotEmpty(webtoon.author, request.getAuthor()));

        if (searchBuilder.hasValue()) {
            builder.and(searchBuilder);
        }

        builder.and(equalsIfNotNull(webtoon.age, request.getAge()));
        builder.and(equalsIfNotNull(webtoon.status, request.getStatus()));
        builder.and(equalsIfNotNull(webtoon.platform, request.getPlatform()));
        builder.and(genreKeywordIdsIn(request.getGenreKeywordIds()));

        return builder;
    }

    private BooleanExpression containsIfNotEmpty(StringExpression path, String value) {
        return StringUtils.hasText(value) ? path.containsIgnoreCase(value) : null;
    }

    private <T> BooleanExpression equalsIfNotNull(SimpleExpression<T> path, T value) {
        return value != null ? path.eq(value) : null;
    }

    private BooleanExpression genreKeywordIdsIn(List<Long> genreKeywordIds) {
        return (genreKeywordIds != null && !genreKeywordIds.isEmpty())
                ? webtoonGenreKeyword.genreKeyword.genreKeywordId.in(genreKeywordIds)
                : null;
    }

    private OrderSpecifier<?> createOrderSpecifier(WebtoonSortType sortType) {
        return switch (sortType) {
            // todo: add more cases
            case TITLE_ASC -> webtoon.title.asc();
            case TITLE_DESC -> webtoon.title.desc();
            default -> webtoon.title.asc();
        };
    }
}
