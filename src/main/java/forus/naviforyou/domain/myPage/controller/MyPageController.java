package forus.naviforyou.domain.myPage.controller;

import forus.naviforyou.domain.myPage.dto.reponse.ResultRes;
import forus.naviforyou.domain.myPage.dto.request.DeleteReq;
import forus.naviforyou.domain.myPage.dto.request.MyPageReq;
import forus.naviforyou.domain.myPage.service.MyPageService;
import forus.naviforyou.global.common.BaseResponse;
import forus.naviforyou.global.common.collection.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/myPage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @PostMapping
    public  ResponseEntity<?> myPage(@RequestBody MyPageReq myPageReq, @AuthenticationPrincipal Member member){
        myPageService.checkPassword(member, myPageReq.getPassword());
        return BaseResponse.ok(
                new ResultRes(true)
        );
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody DeleteReq deleteReq, @AuthenticationPrincipal Member member){
        myPageService.delete(deleteReq.getPwd(), member);
        return BaseResponse.ok(
                new ResultRes(true)
        );
    }
}
