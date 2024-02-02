package forus.naviforyou.domain.myPage.controller;

import forus.naviforyou.domain.myPage.dto.request.ChangePwdReq;
import forus.naviforyou.domain.myPage.dto.reponse.ResultRes;
import forus.naviforyou.domain.myPage.dto.request.DeleteReq;
import forus.naviforyou.domain.myPage.dto.request.MyPageReq;
import forus.naviforyou.domain.myPage.service.MyPageService;
import forus.naviforyou.global.common.BaseResponse;
import forus.naviforyou.global.common.collection.member.Member;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/myPage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @ApiOperation(tags = "3. My Page", value = "확인", notes = "비밀번호를 체크합니다")
    @PostMapping
    public  ResponseEntity<?> myPage(@RequestBody MyPageReq myPageReq, @AuthenticationPrincipal Member member){
        myPageService.checkPassword(member, myPageReq.getPassword());
        return BaseResponse.ok(
                new ResultRes(true)
        );
    }

    @ApiOperation(tags = "3. My Page", value = "탈퇴", notes = "탈퇴합니다")
    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody DeleteReq deleteReq, @AuthenticationPrincipal Member member){
        myPageService.delete(deleteReq.getPwd(), member);
        return BaseResponse.ok(
                new ResultRes(true)
        );
    }

    @ApiOperation(tags = "3. My Page", value = "비밀번호 변경", notes = "비밀번호를 변경합니다")

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangePwdReq req, @AuthenticationPrincipal Member member){
        myPageService.changePwd(req.getNewPassword(), member);
        return BaseResponse.ok(
                new ResultRes(true)
        );
    }
}
