package com.toongather.toongather.domain.genrekeyword.repository;

import com.toongather.toongather.domain.genrekeyword.domain.GenreKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GenreKeywordRepository extends JpaRepository<GenreKeyword, Long> {

    List<GenreKeyword> findAllByGenreKeywordIdIn(List<Long> genreKeywordIds);
}
