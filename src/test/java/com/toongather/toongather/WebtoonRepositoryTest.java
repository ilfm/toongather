package com.toongather.toongather;


import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.repository.ReviewRepository;
import com.toongather.toongather.domain.webtoon.repository.WebtoonRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebtoonRepositoryTest {

    @Autowired
    private WebtoonRepository webtoonRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void webtoonSave(){
        System.out.println(" = ");
        webtoonRepository.findById("22");
    }



}
