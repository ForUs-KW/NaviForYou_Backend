package forus.naviforyou.domain.member.controller;

import forus.naviforyou.domain.member.dto.request.SignUpReq;
import forus.naviforyou.domain.member.service.MemberService;
import forus.naviforyou.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpReq signUpReq){
        memberService.signUp(signUpReq);
        return BaseResponse.ok("회원가입 성공");
    }
}
