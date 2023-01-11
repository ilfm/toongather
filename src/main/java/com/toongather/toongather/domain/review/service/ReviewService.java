package com.toongather.toongather.domain.review.service;


import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    
    /**
     * 리뷰 등록
     * */
    public void saveReview(Review review){
        reviewRepository.save(review);
    }

    /**
     *  해당 리뷰 찾기
     * */
    public Review findOneReview(String reviewId){
        return reviewRepository.findById(reviewId);
    }

    /**
     *  전체 리뷰 조회
     * */
    public List<Review> findAllReview(){
        return reviewRepository.findAll();
    }
}
