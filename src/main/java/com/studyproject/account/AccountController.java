package com.studyproject.account;

import com.studyproject.domain.Account;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AccountController {

    private final SignUpFormValidator signUpFormValidator;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    //회원가입 시에 입력한 닉네임 또는 이메일이 db에 이미 존재하면 오류 잡아줌
    @InitBinder("signUpForm")
    public void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(signUpFormValidator);
    }

    //회원가입 폼 핸들러
    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "account/sign-up2";
    }

    //회원가입 처리 핸들러
    @PostMapping("/sign-up")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {
        if (errors.hasErrors()) {
            return "account/sign-up2";
        }

        //회원가입form에 입력한 정보를 db에 저장하고 account엔티티 안에 이메일을 인증했는지 이메일 인증 토큰을 랜덤으로 생성한 후에 회원가입 시 입력한 이메일로 회원가입 시 메일 인증 링크를 보내줌
        Account account = accountService.processNewAccount(signUpForm);

        //로그인 시에 이 사용자가 인증된 사용자임을 SecurityContextHolder에 등록해주는 로직
        accountService.login(account);

        //TODO 회원가입 처리
        return "redirect:/";
    }

    //이메일 인증 기능 처리 핸들러
    @GetMapping("/check-email-token")
    public String checkEmailToken(String token, String email, Model model) {
        Account account = accountRepository.findByEmail(email);
        String view = "account/checked-email";
        if (account == null) {
            model.addAttribute("error", "wrong.email");
            return view;
        }

        log.info("emailToken: " + account.getEmailCheckToken());
        if (!account.isValidToken(token)) {
            model.addAttribute("error", "wrong.token");
            return view;
        }

        //이메일 인증이 완료되었다고 account.emailVerified필드에 true값 넣고, 언제 가입했는지 알기 위해 joinedAt에 LocalDateTime.now()값을 넣는 로직
        account.completeSignUp();
        accountService.login(account);

        model.addAttribute("numberOfUser", accountRepository.count());
        model.addAttribute("nickname", account.getNickname());
        return view;
    }

    //이메일 인증 폼 핸들러
    @GetMapping("/check-email")
    public String checkEmail(@CurrentUser Account account, Model model) {
        model.addAttribute("email", account.getEmail());
        return "account/check-email";
    }

    //이메일 인증 링크 재전송 기능 핸들러
    @GetMapping("/resend-email")
    public String resendEmail(@CurrentUser Account account) {
        accountService.sendSignUpConfirmEmail(account);
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }
}
