package forus.naviforyou.domain.member.service;

import forus.naviforyou.domain.member.dto.request.OAuthSignUp;
import forus.naviforyou.domain.member.dto.request.LogInReq;
import forus.naviforyou.domain.member.dto.response.TokenRes;
import forus.naviforyou.domain.member.dto.naver.NaverResInfo;
import forus.naviforyou.domain.member.dto.naver.NaverResToken;
import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

@RequiredArgsConstructor
@Service
public class NaverService {

    private final MemberService memberService;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Getter
    @Value("${social.naver.params.clientId}")
    private String clientId;

    @Getter
    @Value("${social.naver.params.clientSecret}")
    private String clientSecret;

    @Getter
    @Value("${social.naver.path.redirectUri}")
    private String redirectUri;

    @Getter
    @Value("${social.naver.path.userInfoUrl}")
    private String userInfoUrl;

    @Getter
    @Value("${social.naver.path.tokenUrl}")
    private String tokenUrl;


    public HashMap<String, String> getParameters() {
        HashMap<String, String> params = new HashMap<>();
        params.put("clientId", getClientId());
        params.put("redirectUri", getRedirectUri());
        params.put("state", generateState());
        return params;
    }

    public TokenRes naverLogin(String code, String state) {

        String accessToken = getAccessToken(code, state);
        NaverResInfo userInfoOauthDto = getUserInfo(accessToken);
        TokenRes UserInfo = setUserTokenDto(userInfoOauthDto);

        return UserInfo;
    }

    public String getAccessToken(String authorizationCode, String state) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.set("grant_type", "authorization_code");
        params.set("client_id", getClientId());
        params.set("client_secret", getClientSecret());
        params.set("code", authorizationCode);
        params.set("state", state);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity(getTokenUrl(), request, String.class);

        try {
            return objectMapper.readValue(response.getBody(), NaverResToken.class).getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public NaverResInfo getUserInfo(String accessToken) {
        String response = requestUserInfo(accessToken);

        try {
            JsonNode jsonNode = objectMapper.readTree(response);

            String email = jsonNode.get("response").get("email").asText();
            String nickname = jsonNode.get("response").get("nickname").asText();
            String phoneNumber = jsonNode.get("response").get("mobile").asText();
            String id = jsonNode.get("response").get("id").asText();

            return NaverResInfo.of(email, nickname, phoneNumber,id);
        } catch (JsonMappingException e) {
            throw new BaseException(ErrorCode.NO_SUCH_NAVER_USER);
        } catch (JsonProcessingException e) {
            throw new BaseException(ErrorCode.INVALID_NAVER_USER);
        }
    }

    public String requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        String response = restTemplate.postForEntity(getUserInfoUrl(), request, String.class).getBody();

        return response;
    }

    public TokenRes setUserTokenDto(NaverResInfo userInfoOauthDto) {

        if (memberService.duplicateEmail(userInfoOauthDto.getEmail()).equals(false)) {

            memberService.naverSignUp(
                    OAuthSignUp.builder()
                            .nickname(userInfoOauthDto.getNickname())
                            .email(userInfoOauthDto.getEmail())
                            .password("naver")
                            .build()
            );
        }

        return memberService.logIn(
                LogInReq.builder()
                        .email(userInfoOauthDto.getEmail())
                        .password("naver")
                        .build()
        );
    }

    public String generateState() {
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();
        return state;
    }

}