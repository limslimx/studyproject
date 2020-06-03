package com.studyproject.bookReview;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class BookReviewForm {

    @NotBlank
    @Length(max = 30)
    private String title;

    @NotBlank
    private String content;
}
