package com.toongather.toongather.domain.review.repository;

import com.toongather.toongather.domain.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review,String> {

}
