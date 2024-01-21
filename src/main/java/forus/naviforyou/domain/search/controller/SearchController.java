package forus.naviforyou.domain.search.controller;

import forus.naviforyou.domain.member.dto.request.SignUpReq;
import forus.naviforyou.domain.search.service.SearchService;
import forus.naviforyou.global.common.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;
    @GetMapping("/basic/{name}")
    public String getSearch(@PathVariable String name){
        System.out.println(searchService.naver(name));
        return searchService.naver(name);
    }
//    public ResponseEntity<?> signUp(@RequestBody SignUpReq signUpReq){
//        memberService.signUp(signUpReq);
//        return BaseResponse.ok("회원가입 성공");
//    }
}
