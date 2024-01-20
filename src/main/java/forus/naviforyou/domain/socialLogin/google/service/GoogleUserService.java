package forus.naviforyou.domain.socialLogin.google.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import forus.naviforyou.domain.socialLogin.google.dto.GoogleOAuthToken;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class GoogleUserService {

//    private final Environment env;
    @Autowired
    private RestTemplate restTemplate;

//    private RestTemplate rt = new RestTemplate();
//    private HttpHeaders headers = new HttpHeaders();

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

    public void socialLogin(String code, String registrationId) {
        System.out.println("code = " + code);
        System.out.println("registrationId = " + registrationId);

        String accessToken = getAccessToken(code, registrationId);

        GoogleOAuthToken userResourceNode = getUserResource(accessToken, registrationId);

        String requestUserInfo =  requestUserInfo(userResourceNode);

    }

    public String getAccessToken(String authorizationCode, String registrationId) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "1006311869838-vvcdgl0nl4lbrod36htdp99enu9cgpol.apps.googleusercontent.com");
        params.add("client_secret", "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");
        params.add("code", authorizationCode);
        params.add("redirect_uri", "http://localhost:8080/login/oauth2/code/google");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate
                .postForEntity("https://oauth2.googleapis.com/token", request, String.class);

//        HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
//                new HttpEntity<>(params,headers);
//
//        ResponseEntity<String> response = rt.exchange(
//                "https://oauth2.googleapis.com/token",
//                HttpMethod.POST,
//                googleTokenRequest,
//                String.class
//        );

        System.out.println("[getGoogleProfile] code로 인증을 받은뒤 응답받은 token 값 : {}"+response);

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleOAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), GoogleOAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return oAuthToken.getAccess_token();
    }

    private GoogleOAuthToken getUserResource(String accessToken, String registrationId) {

//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + accessToken);
//        HttpEntity entity = new HttpEntity(headers);
//
//        ResponseEntity<JsonNode> response = rt.exchange(
//                "https://www.googleapis.com/oauth2/v2/userinfo",
//                HttpMethod.POST,
//                entity,
//                JsonNode.class
//        );
//
//
//        return response.getBody();
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        HttpHeaders header2 = new HttpHeaders();
        // 헤더에 토큰값 설정
        header2.add("Authorization", "Bearer "+accessToken);

        HttpEntity<MultiValueMap<String,String>>googleProfileRequest = new HttpEntity<>(header2);

        ResponseEntity<String> googleProfileResponse = rt.exchange(
                "https://www.googleapis.com/oauth2/v2/userinfo",
                HttpMethod.GET,
                googleProfileRequest,
                String.class
        );
        System.out.println("[google] 구글 프로필 response :{}"+googleProfileResponse);

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleOAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(googleProfileResponse.getBody(), GoogleOAuthToken.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return oAuthToken;
    }

    private String requestUserInfo(GoogleOAuthToken googleOAuthToken){
        return "a";
    }
}


