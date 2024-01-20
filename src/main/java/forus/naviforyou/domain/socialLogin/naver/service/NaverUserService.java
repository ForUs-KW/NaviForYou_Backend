package forus.naviforyou.domain.socialLogin.naver.service;

public interface NaverUserService {
    String getAccessToken(String authorizationCode, String state);
}
