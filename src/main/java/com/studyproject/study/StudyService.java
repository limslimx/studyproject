package com.studyproject.study;

import com.studyproject.domain.Account;
import com.studyproject.domain.Study;
import com.studyproject.study.form.StudyForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class StudyService {

    private final StudyRepository studyRepository;


    public Study createNewStudy(StudyForm studyForm, Account account) {
        log.info("########account:"+account);
        log.info("########account.email:"+account.getEmail());
        Study study = Study.builder()
                .path(studyForm.getPath())
                .title(studyForm.getTitle())
                .managers(new HashSet<>())
                .shortDescription(studyForm.getShortDescription())
                .fullDescription(studyForm.getFullDescription())
                .build();
        study.addManager(account);

        Study newStudy = studyRepository.save(study);

        return newStudy;
    }
}
