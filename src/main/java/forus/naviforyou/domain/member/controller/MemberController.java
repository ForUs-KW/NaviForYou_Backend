package forus.naviforyou.domain.member.controller;

import forus.naviforyou.domain.member.dto.request.KakaoReq;
import forus.naviforyou.domain.member.dto.request.LogInReq;
import forus.naviforyou.domain.member.dto.request.SignUpReq;
import forus.naviforyou.domain.member.dto.response.TokenRes;
import forus.naviforyou.domain.member.service.KakaoService;
import forus.naviforyou.domain.member.service.MemberService;
import forus.naviforyou.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final KakaoService kakaoService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpReq signUpReq){
        memberService.signUp(signUpReq);
        return BaseResponse.ok("회원가입 성공");
    }

    @PostMapping("/login")
    private ResponseEntity<?> signUp(@RequestBody LogInReq logInReq){
        TokenRes token = memberService.logIn(logInReq);
        return BaseResponse.ok(token);
    }

    @PostMapping("/kakao")
    public ResponseEntity<?> kakao(@RequestBody KakaoReq kakaoReq){
        TokenRes tokenRes = kakaoService.KakaoLogin(kakaoReq);
        return BaseResponse.ok(tokenRes);
    }
}
