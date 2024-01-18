package forus.naviforyou.global.socialLogin.naver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import forus.naviforyou.global.common.BaseResponse;
import forus.naviforyou.global.socialLogin.naver.dto.MemberInfoOauthDto;
import forus.naviforyou.global.socialLogin.naver.service.NaverUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member/login")
public class NaverUserController {

    private final BaseResponse baseResponse;
    private final NaverUserServiceImpl naverUserService;

    @RequestMapping("/naver/params")
    public ResponseEntity getNaverParams() {
        return baseResponse.ok(naverUserService.getParameters());
    }

    @RequestMapping("/naver/tokens")
    public ResponseEntity getUserTokenDto(@RequestParam String code,
                                          @RequestParam(required = false) String state) throws JsonProcessingException{

        MemberInfoOauthDto userTokenDto = naverUserService.getUserToken(code, state);
        return baseResponse.ok(userTokenDto);

    }
}
