package com.studyproject.bookReview;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class BookReviewForm {

    @NotBlank
    @Length(max = 100)
    private String title;

    @NotBlank
    private String content;
}
