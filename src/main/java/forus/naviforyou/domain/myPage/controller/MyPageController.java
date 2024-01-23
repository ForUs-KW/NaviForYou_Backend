package forus.naviforyou.domain.myPage.controller;

import forus.naviforyou.domain.myPage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/myPage")
@RequiredArgsConstructor
public class MyPageController {
    private final MyPageService myPageService;
}
