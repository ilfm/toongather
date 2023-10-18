package com.toongather.toongather.domain.review.service;


import com.toongather.toongather.domain.review.domain.Review;
import com.toongather.toongather.domain.review.dto.ReviewDto;
import com.toongather.toongather.domain.review.repository.ReviewRepository;
import javax.persistence.Tuple;
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
    private  ReviewRepository reviewRepository;

    /**
     * 리뷰 생성
     * */
    public void createReview(){}


    /**
     * 리뷰 등록
     * */
    @Transactional
    public void saveReview(Review review){
        reviewRepository.save(review);
    }

    /**
     *  해당 리뷰 찾기
     * */
    public Tuple findOneReview(String reviewId){
        return reviewRepository.findReview(reviewId);
    }

    /**
     *  전체 리뷰 조회
     * */
    public List<ReviewDto> findAllReview(){

        List<ReviewDto> allReview = reviewRepository.findAll();
        allReview.forEach((v)->{
            System.out.println("v.getStar() = " + v.getStar());
            System.out.println("v.getRecommendComment() = " + v.getRecommendComment());
            //System.out.println("v.getMember() = " + v.getMember());
            //System.out.println("v.getWebtoon() = " + v.getWebtoon());
        });
        return allReview;
    }
}
