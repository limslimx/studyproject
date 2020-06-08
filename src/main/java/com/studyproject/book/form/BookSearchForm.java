package com.studyproject.book.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@Data
public class BookSearchForm {

    @NotBlank
    private String searchBy;
}
