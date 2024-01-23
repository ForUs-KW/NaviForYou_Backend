package forus.naviforyou.domain.myPage.controller;

import forus.naviforyou.domain.myPage.dto.reponse.ResultRes;
import forus.naviforyou.domain.myPage.dto.request.DeleteReq;
import forus.naviforyou.domain.myPage.service.MyPageService;
import forus.naviforyou.global.common.BaseResponse;
import forus.naviforyou.global.common.collection.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/myPage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;

    @PostMapping("/delete")
    private ResponseEntity<?> delete(@RequestBody DeleteReq deleteReq, @AuthenticationPrincipal Member member){
        myPageService.delete(deleteReq.getPwd(), member);
        return BaseResponse.ok(
                new ResultRes(true)
        );
    }
}
