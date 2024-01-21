package forus.naviforyou.domain.member.controller;

import forus.naviforyou.domain.member.dto.request.CheckSingUpCodeReq;
import forus.naviforyou.domain.member.dto.request.LogInReq;
import forus.naviforyou.domain.member.dto.request.SignUpReq;
import forus.naviforyou.domain.member.dto.response.DuplicateRes;
import forus.naviforyou.domain.member.dto.response.TokenRes;
import forus.naviforyou.domain.member.service.GoogleService;
import forus.naviforyou.domain.member.service.KakaoService;
import forus.naviforyou.domain.member.service.MemberService;
import forus.naviforyou.domain.member.service.NaverService;
import forus.naviforyou.global.common.BaseResponse;
import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final KakaoService kakaoService;
    private final NaverService naverService;
    private final GoogleService googleService;


    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpReq signUpReq){
        memberService.signUp(signUpReq);
        return BaseResponse.ok("회원가입 성공");
    }

    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody LogInReq logInReq){
        TokenRes token = memberService.logIn(logInReq);
        return BaseResponse.ok(token);
    }

    @GetMapping("/emailDuplicate")
    private ResponseEntity<?> emailDuplicate(String email){
        if(memberService.duplicateEmail(email)){
            throw new BaseException(ErrorCode.DUPLICATE_EMAIL);
        }
        return BaseResponse.ok(
                DuplicateRes.builder()
                        .result(true)
                        .build()
        );
    }

    @GetMapping("/nicknameDuplicate")
    private ResponseEntity<?> nicknameDuplicate(String nickname){
        if(memberService.duplicateNickname(nickname)){
            throw new BaseException(ErrorCode.DUPLICATE_NICKNAME);
        }
        return BaseResponse.ok(
                DuplicateRes.builder()
                        .result(true)
                        .build()
        );
    }

    @GetMapping("/sendEmailCode")
    private ResponseEntity<?> sendEmailCode(String email){
        memberService.sendEmailCode(email);
        return BaseResponse.ok(
                DuplicateRes.builder()
                        .result(true)
                        .build()
        );
    }

    @PostMapping("/checkEmailCode")
    private ResponseEntity<?> checkEmailCode(@RequestBody CheckSingUpCodeReq req){
        memberService.checkEmailCode(req);
        return BaseResponse.ok(
                DuplicateRes.builder()
                        .result(true)
                        .build()
        );
    }

    @GetMapping("/kakao")
    public ResponseEntity<?> kakao(String code){
        TokenRes tokenRes = kakaoService.KakaoLogin(code);
        return BaseResponse.ok(tokenRes);
    }

    @GetMapping("/naver")
    public ResponseEntity<?> naver(String code,String state){
        TokenRes tokenRes = naverService.naverLogin(code,state);
        return BaseResponse.ok(tokenRes);
    }

    @GetMapping("/google")
    public ResponseEntity<?> google(String code,String state){
        TokenRes tokenRes = googleService.googleLogin(code,state);
        return BaseResponse.ok(tokenRes);
    }
}
