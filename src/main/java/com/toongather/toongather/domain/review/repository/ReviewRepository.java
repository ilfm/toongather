package com.toongather.toongather.domain.review.repository;


import com.toongather.toongather.domain.review.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {

    @PersistenceContext
    private EntityManager em;

    public List<Review> findAll(){
        return em.createQuery("select r from Review r",Review.class ).getResultList();
    }

    public Review findById(String reviewId){
        return em.find(Review.class,reviewId);
    }

    public void save(Review review){
        if(review.getReviewId() == null){
            em.persist(review);
        }else{
            em.merge(review);
        }

    }


}
