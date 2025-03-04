package com.toongather.toongather.domain.keyword.service;

import com.toongather.toongather.domain.keyword.domain.Keyword;
import com.toongather.toongather.domain.keyword.dto.KeywordDto;
import com.toongather.toongather.domain.keyword.repository.KeywordRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KeywordService {

  private final KeywordRepository keywordRepository;

  @Autowired
  public KeywordService(KeywordRepository keywordRepository) {
    this.keywordRepository = keywordRepository;
  }

  @Transactional
  public Long saveKeyword(Keyword keyword) {
    if (keyword.getKeywordId() == null) {
      keywordRepository.save(keyword);
    }
    return keyword.getKeywordId();
  }

  @Transactional
  public Keyword createKeyword(String keywordNm) {
    return keywordRepository.findByKeywordNm(keywordNm)
        .orElseGet(() -> {
          Long keywordId = saveKeyword(Keyword.createKeyword(keywordNm));
          return keywordRepository.findById(keywordId)
              .orElse(null);
        });
  }

  public Optional<KeywordDto> findByKeywordNm(String keywordNm) {
    return keywordRepository.findByKeywordNm(keywordNm)
        .map(keyword -> KeywordDto.builder()
            .keywordId(keyword.getKeywordId())
            .keywordNm(keyword.getKeywordNm()).build());
  }

}
