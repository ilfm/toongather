package com.toongather.toongather;


import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.repository.ReviewJpaRepository;
import com.toongather.toongather.domain.webtoon.domain.Age;
import com.toongather.toongather.domain.webtoon.domain.Platform;
import com.toongather.toongather.domain.webtoon.domain.Webtoon;
import com.toongather.toongather.domain.webtoon.domain.WebtoonStatus;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest

public class WebtoonRepositoryTest {

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void 웹툰등록_테스트데이터넣기용(){
        System.out.println(" = ");
        Webtoon build = Webtoon.builder()
                        .toonId("1000000721433")
                        .title("집이 없어")
                        .age(Age.ALL)
                        .author("와난")
                        .status(WebtoonStatus.ING)
                        .platform(Platform.NAVER)
                        .imgPath("https://image-comic.pstatic.net/webtoon/721433/thumbnail/thumbnail_IMAG21_c907f727-e522-4517-952e-398ea95d2efb.jpg").build();

        webtoonRepository.save(build);
    }
}
