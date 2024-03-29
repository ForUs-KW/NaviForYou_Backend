package forus.naviforyou.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import forus.naviforyou.domain.member.dto.kakao.KakaoResProfileInfo;
import forus.naviforyou.domain.member.dto.kakao.KakaoResToken;
import forus.naviforyou.domain.member.dto.request.OAuthSignUp;
import forus.naviforyou.domain.member.dto.request.LogInReq;
import forus.naviforyou.domain.member.dto.response.TokenRes;
import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final MemberService memberService;

    @Value("${kakao.secret.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${kakao.redirect.url}")
    private String KAKAO_REDIRECT_URL;

    @Transactional
    public TokenRes KakaoLogin(String code) {
        //code 이용 하여 oAuthAccessToken 얻어옴
        KakaoResToken kaKaoOAuthToken = getKakaoToken(code);
        //oAuthAccessToken 으로 KakaoResProfileInfo 가져옴
        KakaoResProfileInfo kakaoProfile = getKakaoUserInfo(kaKaoOAuthToken);
        String email = kakaoProfile.getKakaoAccount().getEmail();
        String id = kakaoProfile.getId().toString();
        // 해당 profile 으로 된 계정이 있는지 확인
        // 없다면 회원가입 후 로그인
        if (memberService.duplicateEmail(email).equals(false)) {

            memberService.kakaoSignUp(
                    OAuthSignUp.builder()
                            .nickname(kakaoProfile.getProperties().getNickname())
                            .email(email)
                            .password(id)
                            .build()
            );
        }

        // 있다면 로그인
        return memberService.logIn(
                LogInReq.builder()
                .email(email)
                .password(id)
                .build()
        );
    }

    private KakaoResToken getKakaoToken(String code) {
        try {
            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // HTTP Body 생성
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", KAKAO_CLIENT_ID);
            body.add("redirect_uri", KAKAO_REDIRECT_URL);
            body.add("code", code);

            // HTTP 요청 보내기
            HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );

            // HTTP 응답 (JSON) -> 액세스 토큰 파싱
            //response objectMapper 로 파싱 하여 oAuthAccessToken 얻어냄
            ObjectMapper objectMapper = new ObjectMapper();
            KakaoResToken kakaoResToken = objectMapper.readValue(response.getBody(), KakaoResToken.class);


            log.info("Kakao Token ={}",kakaoResToken);

            return kakaoResToken;
        } catch (JsonProcessingException e) {
            log.info("GET_OAUTH_TOKEN_FAILED: ",e);
            throw new BaseException(ErrorCode.GET_OAUTH_TOKEN_FAILED);
        }

    }

    private KakaoResProfileInfo getKakaoUserInfo(KakaoResToken kakaoResToken) {
        try {
            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + kakaoResToken.getAccessToken());
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            // HTTP 요청 보내기
            HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
            RestTemplate rt = new RestTemplate();
            ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoUserInfoRequest,
                    String.class
            );

            log.info("Kakao response={}",response.getBody());
            ObjectMapper objectMapper = new ObjectMapper();
            KakaoResProfileInfo kakaoResProfileInfo = objectMapper.readValue(response.getBody(), KakaoResProfileInfo.class);
            log.info("KakaoResProfileInfo={}",kakaoResProfileInfo);

            return kakaoResProfileInfo;
        } catch (JsonProcessingException e) {
            log.info("GET_OAUTH_USER_INFO_FAILED: ",e);
            throw new BaseException(ErrorCode.GET_OAUTH_USER_INFO_FAILED);
        }
    }


}
