package forus.naviforyou.domain.member.service;

import forus.naviforyou.domain.member.dto.request.CheckSingUpCodeReq;
import forus.naviforyou.domain.member.dto.request.OAuthSignUp;
import forus.naviforyou.domain.member.dto.request.LogInReq;
import forus.naviforyou.domain.member.dto.request.SignUpReq;
import forus.naviforyou.domain.member.dto.response.TokenRes;
import forus.naviforyou.domain.member.repository.MemberRepository;
import forus.naviforyou.global.common.Constants;
import forus.naviforyou.global.common.collection.enums.MemberType;
import forus.naviforyou.global.common.collection.enums.Role;
import forus.naviforyou.global.common.collection.member.Member;
import forus.naviforyou.global.common.service.RedisService;
import forus.naviforyou.global.config.jwt.JwtTokenProvider;
import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MailService mailService;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisService redisService;

    private final int CODE_LENGTH = 6;
    private final long CODE_MINUTE = 10;
    public void signUp(SignUpReq signUpReq){
        memberRepository.save(
                Member.builder()
                        .nickname(signUpReq.getNickname())
                        .email(signUpReq.getEmail())
                        .phone(signUpReq.getPhone())
                        .password(passwordEncoder.encode(signUpReq.getPassword()))
                        .memberType(MemberType.GENERAL)
                        .role(Role.ROLE_USER)
                        .memberType(MemberType.GENERAL)
                        .build()
        );
    }

    public TokenRes logIn(LogInReq logInReq){
        Member member = memberRepository.findByEmail(logInReq.getEmail()).orElseThrow(
                () -> new BaseException(ErrorCode.NO_SUCH_EMAIL)
        );

        if (!passwordEncoder.matches(logInReq.getPassword(), member.getPassword())) {
            throw new BaseException(ErrorCode.WRONG_PASSWORD);
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                logInReq.getEmail(), logInReq.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject()
                .authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.createToken(authentication);
        String token = "Bearer " + jwt;

        return TokenRes.builder()
                .accessToken(token)
                .build();
    }

    public Boolean duplicateEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public Boolean duplicateNickname(String nickname) {
        return memberRepository.existsByNickname(nickname);
    }

    public void kakaoSignUp(OAuthSignUp signUpReq){
        memberRepository.save(
                Member.builder()
                        .nickname(signUpReq.getNickname())
                        .email(signUpReq.getEmail())
                        .password(passwordEncoder.encode(signUpReq.getPassword()))
                        .memberType(MemberType.KAKAO)
                        .role(Role.ROLE_USER)
                        .build()
        );
    }

    public void googleSignUp(OAuthSignUp signUpReq){
        memberRepository.save(
                Member.builder()
                        .nickname(signUpReq.getNickname())
                        .email(signUpReq.getEmail())
                        .password(passwordEncoder.encode(signUpReq.getPassword()))
                        .memberType(MemberType.GOOGLE)
                        .role(Role.ROLE_USER)
                        .build()
        );
    }

    public void naverSignUp(OAuthSignUp signUpReq){
        memberRepository.save(
                Member.builder()
                        .nickname(signUpReq.getNickname())
                        .email(signUpReq.getEmail())
                        .password(passwordEncoder.encode(signUpReq.getPassword()))
                        .memberType(MemberType.NAVER)
                        .role(Role.ROLE_USER)
                        .build()
        );
    }


    public void sendEmailCode(String email) {
        String code = makeRandomNumber();
        String title = "회원 가입 인증 이메일 입니다.";
        String content =
                "인증번호 : " +  code +
                        "<br> 유효시간은 " + CODE_MINUTE + "분 입니다." +
                        "<br> 인증번호를 제대로 입력해주세요";

        mailService.mailSend(email,title,content);
        redisService.setValues(email+ Constants.SIGN_UP_FLAG, code, Duration.ofMinutes(CODE_MINUTE));

    }

    public void checkEmailCode(CheckSingUpCodeReq req){
        String key = req.getEmail()+Constants.SIGN_UP_FLAG;
        String code = req.getCode();

        if(!redisService.hasKey(key)){
            throw new BaseException(ErrorCode.EXPIRED_VERIFICATION_CODE);
        }

        if(!code.equals(redisService.getValues(key))){
            throw new BaseException(ErrorCode.INCORRECT_VERIFICATION_CODE);
        }
        redisService.deleteValues(key);
    }

    private String makeRandomNumber() {
        Random r = new Random();
        StringBuilder randomNumber = new StringBuilder();
        for(int i = 0; i < CODE_LENGTH; i++) {
            randomNumber.append(r.nextInt(10));
        }

        return randomNumber.toString();
    }
}
