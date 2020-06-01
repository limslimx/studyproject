package com.studyproject.study;

import com.studyproject.account.CurrentUser;
import com.studyproject.domain.Account;
import com.studyproject.domain.Study;
import com.studyproject.study.form.StudySettingsForm;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Controller
public class StudySettingsController {

    private final StudyService studyService;
    private final StudyRepository studyRepository;
    private final ModelMapper modelMapper;

    @GetMapping("/study/{path}//settings/description")
    public String studySettingView(@CurrentUser Account account, @PathVariable String path, Model model) {
        Study study = studyService.getStudyToUpdate(path, account);
        model.addAttribute("account", account);
        model.addAttribute("study", study);
        model.addAttribute("studySettingsForm", modelMapper.map(study, StudySettingsForm.class));
        return "study/settings/description";
    }

    @PostMapping("/study/{path}//settings/description")
    public String updateStudySetting(@CurrentUser Account account, @PathVariable String path, @Valid StudySettingsForm studySettingsForm, Errors errors, Model model, RedirectAttributes attributes) throws UnsupportedEncodingException {
        Study study = studyService.getStudyByPath(path);
        if (errors.hasErrors()) {
            model.addAttribute("account", account);
            model.addAttribute("study", study);
            return "study/settings/description";
        }
        studyService.updateStudy(study, studySettingsForm);
        attributes.addFlashAttribute("message", "스터디 소개를 수정하였습니다.");
        return "redirect:/study/"+ URLEncoder.encode(path, String.valueOf(StandardCharsets.UTF_8)) +"/settings/description";
    }
}
