package com.toongather.toongather;


import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.repository.ReviewRepository;
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
    public void webtoonSave(){  // 웹툰 등록
        System.out.println(" = ");
        Webtoon build = Webtoon.builder()
                        .title("스터디그룹")
                        .summary("")
                        .writerNm("손제호")
                        .status(WebtoonStatus.ING)
                        .age("12세이용가")
                        .imgPath("/tmp").build();
        // 사용자 등록자 자동넣기 해야함
        webtoonRepository.save(build);
    }

    @Test
    public void findToonById(){

        Webtoon toon = webtoonRepository.findById("WT-4");
        System.out.println("toon.getTitle() = " + toon.getTitle());
    }

    @Test
    @Rollback(false)
    public void testQuerydsl(){
        List<Webtoon> toonList = webtoonRepository.findAll();

        toonList.iterator().forEachRemaining((v)->{
            System.out.println("title = " + v.getTitle());
        });
    }



}
