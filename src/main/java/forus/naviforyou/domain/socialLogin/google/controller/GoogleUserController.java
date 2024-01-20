package forus.naviforyou.domain.socialLogin.google.controller;

import forus.naviforyou.domain.socialLogin.google.service.GoogleUserService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/login/oauth2")
public class GoogleUserController {
    GoogleUserService googleUserService;

    public GoogleUserController(GoogleUserService googleUserService) {
        this.googleUserService = googleUserService;
    }

    @RequestMapping("/code/{registrationId}")
    public void googleLogin(@RequestParam String code, @PathVariable String registrationId) {
        googleUserService.socialLogin(code, registrationId);
        System.out.println(googleUserService.getAccessToken(code,registrationId));
    }
}
