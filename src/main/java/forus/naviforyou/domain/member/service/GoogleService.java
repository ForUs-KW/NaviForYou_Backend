package forus.naviforyou.domain.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import forus.naviforyou.domain.member.dto.request.OAuthSignUp;
import forus.naviforyou.domain.member.dto.request.LogInReq;
import forus.naviforyou.domain.member.dto.response.TokenRes;
import forus.naviforyou.domain.member.dto.google.GoogleResInfo;
import forus.naviforyou.domain.member.dto.google.GoogleResToken;
import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class GoogleService {

    private final MemberService memberService;

    @Autowired
    private RestTemplate restTemplate;

    private RestTemplate rt = new RestTemplate();
    private HttpHeaders headers = new HttpHeaders();

    @Getter
    @Value("${social.google.params.clientId}")
    private String clientId;

    @Getter
    @Value("${social.google.params.clientSecret}")
    private String clientSecret;

    @Getter
    @Value("${social.google.path.redirectUrl}")
    private String redirectUrl;

    @Getter
    @Value("${social.google.path.resourceUrl}")
    private String resourceUrl;

    @Getter
    @Value("${social.google.path.tokenUrl}")
    private String tokenUrl;

    public TokenRes googleLogin(String code) {
        String accessToken = getAccessToken(code);
        GoogleResInfo userResourceNode = getUserResource(accessToken);
        TokenRes requestUserInfo =  requestUserInfo(userResourceNode);
        return requestUserInfo;
    }

    public String getAccessToken(String authorizationCode) {

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", getClientId());
        params.add("client_secret", getClientSecret());
        params.add("code", authorizationCode);
        params.add("redirect_uri", getRedirectUrl());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity("https://oauth2.googleapis.com/token", request, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleResToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), GoogleResToken.class);
        }catch (JsonProcessingException e) {
            throw new BaseException(ErrorCode.GET_OAUTH_TOKEN_FAILED);
        }
        return oAuthToken.getAccess_token();
    }

    private GoogleResInfo getUserResource(String accessToken) {

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        HttpHeaders header2 = new HttpHeaders();

        header2.add("Authorization", "Bearer "+accessToken);

        HttpEntity<MultiValueMap<String,String>>googleProfileRequest = new HttpEntity<>(header2);

        ResponseEntity<String> googleProfileResponse = rt.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                googleProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleResInfo oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(googleProfileResponse.getBody(), GoogleResInfo.class);
        } catch (JsonMappingException e) {
            throw new BaseException(ErrorCode.GET_OAUTH_USER_INFO_FAILED);
        } catch (JsonProcessingException e) {
            throw new BaseException(ErrorCode.GET_OAUTH_USER_INFO_FAILED);
        }



        return oAuthToken;
    }

    private TokenRes requestUserInfo(GoogleResInfo googleInfoDto){

        if (memberService.duplicateEmail(googleInfoDto.getEmail()).equals(false)) {

            memberService.googleSignUp(
                    OAuthSignUp.builder()
                            .nickname(googleInfoDto.getEmail())
                            .email(googleInfoDto.getEmail())
                            .password(googleInfoDto.getId())
                            .build()
            );
        }

        return memberService.logIn(
                LogInReq.builder()
                        .email(googleInfoDto.getEmail())
                        .password(googleInfoDto.getId())
                        .build()
        );
    }
}


