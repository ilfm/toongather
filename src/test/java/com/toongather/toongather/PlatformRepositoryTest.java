package com.toongather.toongather;

import com.toongather.toongather.domain.webtoon.domain.Platform;
import com.toongather.toongather.domain.webtoon.repository.PlatformRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlatformRepositoryTest {

    @Autowired
    PlatformRepository platformRepository;

    @Test
    @Transactional
    public void testPlatform() {
        Platform platform = new Platform();

        platform.setPlatformNm("네이버222");
        platform.setAmdUserId("1");
        platform.setRegUserId("1");
        //platform.setAmdDt(LocalDateTime.now());
        //platform.setRegDt(LocalDateTime.now());
        String id =  platformRepository.save(platform);
        //System.out.println("id = " + id);

    }


    @Test
    @Transactional
    @Rollback(false)
    public void 플랫폼_변경후_업데이트날짜_변경확인() {

        Platform platform =  platformRepository.find("WTPF-44");
        platformRepository.update(platform);
        //System.out.println("id = " + id);

    }
}
