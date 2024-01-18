package forus.naviforyou.global.socialLogin.naver.service;

import forus.naviforyou.global.common.entity.Member;
import forus.naviforyou.global.socialLogin.naver.dto.OauthTokenDto;
import forus.naviforyou.global.socialLogin.naver.dto.MemberInfoOauthDto;
import forus.naviforyou.global.socialLogin.naver.dto.MemberTokenDto;
import forus.naviforyou.global.socialLogin.naver.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
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
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NaverUserServiceImpl implements NaverUserService{

    private final MemberRepository memberepository;
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

    public MemberInfoOauthDto getUserToken(String code, String state) {
        String accessToken = getAccessToken(code, state);
        MemberInfoOauthDto userInfoOauthDto = getUserInfo(accessToken);

        return userInfoOauthDto;

//        return setUserTokenDto(userInfoOauthDto);
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
            return objectMapper.readValue(response.getBody(), OauthTokenDto.class).getAccessToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public MemberInfoOauthDto getUserInfo(String accessToken) {
        String response = requestUserInfo(accessToken);

        try {
            JsonNode jsonNode = objectMapper.readTree(response);

            String email = jsonNode.get("response").get("email").asText();
            String nickname = jsonNode.get("response").get("nickname").asText();
            String phoneNumber = jsonNode.get("response").get("mobile").asText();

            return MemberInfoOauthDto.of(email, nickname, phoneNumber);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return MemberInfoOauthDto.of();
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

//    public MemberTokenDto setUserTokenDto(MemberInfoOauthDto userInfoOauthDto) {
//        Optional<Member> optionalUser = memberepository.findByEmail(userInfoOauthDto.getEmail());
//        Member member;
//
//        if (optionalUser.isPresent()) {
//            member = optionalUser.get();
//        } else {
//            member = userInfoOauthDto.toEntity();
//            member.setRoles(authorityUtils.createRoles(user.getEmail()));
//            memberepository.save(member);
//        }
//
//        MemberTokenDto userTokenDto = MemberTokenDto.of(user);
//        setTokens(userTokenDto);
//        return userTokenDto;
//    }

    public String generateState() {
        SecureRandom random = new SecureRandom();
        String state = new BigInteger(130, random).toString();
        return state;
    }

}