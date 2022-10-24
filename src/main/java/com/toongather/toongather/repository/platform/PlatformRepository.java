package com.toongather.toongather.repository.platform;


import com.toongather.toongather.domain.model.Platform;
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
