package com.toongather.toongather;


import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.repository.ReviewRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    public void 리뷰등록(){

        Review review = new Review();
        reviewRepository.save(review);

    }


    // 리뷰 조회
    public void 모든리뷰조회(){


    }



}
