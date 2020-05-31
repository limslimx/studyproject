package com.studyproject.study;

import com.studyproject.domain.Account;
import com.studyproject.domain.Study;
import com.studyproject.study.form.StudyForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class StudyService {

    private final StudyRepository studyRepository;


    public Study createNewStudy(StudyForm studyForm, Account account) {
        Study study = Study.builder()
                .path(studyForm.getPath())
                .title(studyForm.getTitle())
                .shortDescription(studyForm.getShortDescription())
                .fullDescription(studyForm.getFullDescription())
                .build();
        Study newStudy = studyRepository.save(study);
        newStudy.addManager(account);

        return newStudy;
    }
}
