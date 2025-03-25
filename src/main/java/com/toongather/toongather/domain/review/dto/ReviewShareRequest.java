package com.toongather.toongather.domain.review.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewShareRequest {

  List<Long> reviewIds;
}
