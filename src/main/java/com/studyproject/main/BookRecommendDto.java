package com.studyproject.main;

import lombok.Data;

@Data
public class BookRecommendDto {

    private String bookCategory;
    private Long bookCategoryCount;

    public BookRecommendDto(String bookCategory, Long bookCategoryCount) {
        this.bookCategory = bookCategory;
        this.bookCategoryCount = bookCategoryCount;
    }
}
