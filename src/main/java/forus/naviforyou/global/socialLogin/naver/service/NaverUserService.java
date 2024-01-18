package forus.naviforyou.global.socialLogin.naver.service;

public interface NaverUserService {
    String getAccessToken(String authorizationCode, String state);
}
