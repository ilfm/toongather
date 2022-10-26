package com.toongather.toongather.domain.webtoon.repository;


import com.toongather.toongather.domain.webtoon.domain.Platform;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PlatformRepository {

    @PersistenceContext
    EntityManager em;
    public String save(Platform platform){
        em.persist(platform);
        return platform.getPlatformId();
    }
}
