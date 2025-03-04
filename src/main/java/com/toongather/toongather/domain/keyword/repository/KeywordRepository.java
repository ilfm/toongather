package com.toongather.toongather.domain.keyword.repository;

import com.toongather.toongather.domain.keyword.domain.Keyword;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

  public Optional<Keyword> findByKeywordNm(String keywordNm);
}
