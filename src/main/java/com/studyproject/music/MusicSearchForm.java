package com.studyproject.music;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Data
public class MusicSearchForm {

    @NotBlank
    private String searchBy;
}
