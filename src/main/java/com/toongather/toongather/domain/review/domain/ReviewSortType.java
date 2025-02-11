package com.toongather.toongather.domain.review.domain;

public enum ReviewSortType {

  CREATE_DATE_DESC("최신순"),
  STAR_DESC("별점높은순"),
  STAR_ASC("별점낮은순");

  private String text;

  private ReviewSortType(String text){
    this.text = text;
  }
}
