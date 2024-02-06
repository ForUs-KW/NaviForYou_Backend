package forus.naviforyou.domain.member.controller;

import forus.naviforyou.domain.member.dto.request.*;
import forus.naviforyou.domain.member.dto.response.DeleteRes;
import forus.naviforyou.domain.member.dto.response.DuplicateRes;
import forus.naviforyou.domain.member.dto.response.TokenRes;
import forus.naviforyou.domain.member.service.GoogleService;
import forus.naviforyou.domain.member.service.KakaoService;
import forus.naviforyou.domain.member.service.MemberService;
import forus.naviforyou.domain.member.service.NaverService;
import forus.naviforyou.global.common.BaseResponse;
import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(tags = "2. Member", value = "회원가입", notes = "회원가입을 합니다")
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody SignUpReq signUpReq){
        memberService.signUp(signUpReq);
        return BaseResponse.ok("회원가입 성공");
    }

    @ApiOperation(tags = "2. Member", value = "로그인", notes = "로그인을 합니다")
    @PostMapping("/login")
    private ResponseEntity<?> login(@RequestBody LogInReq logInReq){
        TokenRes token = memberService.logIn(logInReq);
        return BaseResponse.ok(token);
    }

    @ApiOperation(tags = "2. Member", value = "이메일 중복 확인", notes = "이메일이 중복인지 확인합니다")
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

    @ApiOperation(tags = "2. Member", value = "닉네임 중복 확인", notes = "닉네임이 중복인지 확인합니다")
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

    @ApiOperation(tags = "2. Member", value = "이메일 코드 전송", notes = "이메일 코드를 전송합니다")
    @GetMapping("/sendEmailCode")
    private ResponseEntity<?> sendEmailCode(String email){
        memberService.sendEmailCode(email);
        return BaseResponse.ok(
                DuplicateRes.builder()
                        .result(true)
                        .build()
        );
    }

    @ApiOperation(tags = "2. Member", value = "이메일 코드 인증", notes = "이메일 코드 인증을 확인합니다")
    @PostMapping("/checkEmailCode")
    private ResponseEntity<?> checkEmailCode(@RequestBody CheckCodeReq req){
        memberService.checkEmailCode(req);
        return BaseResponse.ok(
                DuplicateRes.builder()
                        .result(true)
                        .build()
        );
    }

    @ApiOperation(tags = "2. Member", value = "카카오 소셜 로그인 요청", notes = "카카오로 소셜 로그인 요청을 보냅니다")
    @GetMapping("/kakao")
    public ResponseEntity<?> kakao(String code){
        TokenRes tokenRes = kakaoService.KakaoLogin(code);
        return BaseResponse.ok(tokenRes);
    }

    @ApiOperation(tags = "2. Member", value = "네이버 소셜 로그인 요청", notes = "네이버로 소셜 로그인 요청을 보냅니다")
    @GetMapping("/naver")
    public ResponseEntity<?> naver(String code,String state){
        TokenRes tokenRes = naverService.naverLogin(code,state);
        return BaseResponse.ok(tokenRes);
    }

    @ApiOperation(tags = "2. Member", value = "구글 소셜 로그인 요청", notes = "구글로 소셜 로그인 요청을 보냅니다.")
    @GetMapping("/google")
    public ResponseEntity<?> google(String code){
        TokenRes tokenRes = googleService.googleLogin(code,"state");
        return BaseResponse.ok(tokenRes);
    }


    @ApiOperation(tags = "2. Member", value = "비밀번호 찾기", notes = "비밀번호를 찾습니다")
    @GetMapping("/searchPassword/sendCode")
    public ResponseEntity<?> searchPwdChkCode(@RequestParam String email){
        memberService.sendEmailCode(email);
        return BaseResponse.ok(
                DeleteRes.builder()
                        .result(true)
                        .build()
        );
    }

    @ApiOperation(tags = "2. Member", value = "비밀번호 확인", notes = "비밀번호를 확인합니다")
    @PostMapping("/searchPassword/checkCode")
    public ResponseEntity<?> searchPwdChkCode(@RequestBody CheckCodeReq req){
        memberService.checkEmailCode(req);
        return BaseResponse.ok(
                DeleteRes.builder()
                        .result(true)
                        .build()
        );
    }

    @ApiOperation(tags = "2. Member", value = "비밀번호 변경", notes = "비밀번호를 변경합니다")
    @PostMapping("/searchPassword/change")
    public ResponseEntity<?> searchPwdChange(@RequestBody ChangePwdReq req){
        memberService.changePwd(req);
        return BaseResponse.ok(
                DeleteRes.builder()
                        .result(true)
                        .build()
        );
    }

}
