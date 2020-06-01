package com.studyproject.study;

import com.studyproject.domain.Account;
import com.studyproject.domain.Study;
import com.studyproject.study.form.StudyForm;
import com.studyproject.study.form.StudySettingsForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@RequiredArgsConstructor
@Transactional
@Service
public class StudyService {

    private final StudyRepository studyRepository;
    private final ModelMapper modelMapper;

    public Study createNewStudy(StudyForm studyForm, Account account) {
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

    public Study getStudyToUpdate(String path, Account account) {
        Study study = getStudyByPath(path);
        if (!account.isManagerOf(study)) {
            throw new AccessDeniedException("권한이 없어서 해당 기능을 사용할 수 없습니다.");
        }
        return study;
    }

    public Study getStudyByPath(String path) {
        Study study = studyRepository.findByPath(path);
        if (study == null) {
            throw new IllegalArgumentException(path + "에 해당하는 스터디가 존재하지 않습니다.");
        }
        return study;
    }

    public void updateStudy(Study study, StudySettingsForm studySettingsForm) {
        modelMapper.map(studySettingsForm, study);

    }
}
