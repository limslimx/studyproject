package com.studyproject.account;

import com.studyproject.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    /*accountRepository.save()를 실행한 후에는 newAccount는 영속상태가 아닌 detached상태인데
   그렇기 때문에 newAccount.generateEmailCheckToken()을 실행해도 영속상태가 아니기 때문에 변경감지가 일어나지 않고 db에 값이 저장되지 않는다.
   이 문제를 해결하기 위해서 @Transactional 어노테이션을 이용하여 processNewAccount() 메서드 안은 하나의 트랜잭션이 유지되고 있음을 지정해주어야 한다.
   이렇게 함으로써 newAccount 객체는 detached 상태가 아니라 영속 상태로 만들어줄 수 있다.
     */
    @Transactional
    public Account processNewAccount(SignUpForm signUpForm) {
        Account newAccount = saveNewAccount(signUpForm);
        newAccount.generateEmailCheckToken();
        sendSignUpConfirmEmail(newAccount);

        return newAccount;
    }

    private Account saveNewAccount(@Valid SignUpForm signUpForm) {
        Account account = Account.builder()
                .nickname(signUpForm.getNickname())
                .email(signUpForm.getEmail())
                .password(passwordEncoder.encode(signUpForm.getPassword()))
                .studyCreatedByWeb(true)
                .studyEnrollmentResultByWeb(true)
                .studyUpdateByWeb(true)
                .build();
        return accountRepository.save(account);
    }

    public void sendSignUpConfirmEmail(Account newAccount) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(newAccount.getEmail());
        mailMessage.setSubject("독서할래 회원가입 인증 메일");
        mailMessage.setText("/check-email-token?token=" + newAccount.getEmailCheckToken() + "&email=" + newAccount.getEmail());
        javaMailSender.send(mailMessage);
    }

    public void login(Account account) {
        //principal은 여기서 인증 시에 authentication에 들어있는 첫번째 파라미터 값이다. 즉, 여기서는 account.getNickname() 이라고 생각하면 된다
        //인증(로그인)을 안한 상태에서 접근하는 경우에는 principal이 anonymousUser라는 문자열값이 들어간다.
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(new UserAccount(account), account.getPassword(), Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    //UserDetailsService를 implement하고 아래의 메서드를 오버라이딩 하기만 하면 Spring Security에서 자동으로 로그인 처리를 해준다. 즉, 여기서는 사용자가 로그인폼에서 입력한 값이 계정db에 존재하는지 확인해주는 코드만 작성하면 된다.
    @Override
    public UserDetails loadUserByUsername(String emailOrNickname) throws UsernameNotFoundException {

        //db에서 우선 email값으로 계정 정보가 존재하는지 확인해보고 없으면 nickname값으로 확인함
        Account account = accountRepository.findByEmail(emailOrNickname);
        if (account == null) {
            account = accountRepository.findByNickname(emailOrNickname);
        }

        if (account == null) {
            throw new UsernameNotFoundException(emailOrNickname);
        }

        //우리가 principal값으로 썼던 UserAccount 객체를 사용함
        return new UserAccount(account);
    }
}
