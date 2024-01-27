package forus.naviforyou.domain.findRoute.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import forus.naviforyou.domain.findRoute.dto.request.WalkRouteReq;
import forus.naviforyou.domain.findRoute.dto.response.WalkRouteRes;
import forus.naviforyou.domain.search.dto.response.SearchRes;
import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class FindRouteService {

    @Value("${tmap.app-key}")
    private String appKey;

    private RestTemplate restTemplate = new RestTemplate();


    public void getWalkRoute(WalkRouteReq walkRouteReq){

        // API 엔드포인트 URL
        String apiUrl = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1";

        // API Key
        String apiKey = appKey;

        // 요청 데이터

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.set("appKey", apiKey);

        // 요청 엔터티 생성
        HttpEntity<WalkRouteReq> requestEntity = new HttpEntity<>(walkRouteReq, headers);

        // HTTP POST 요청 보내기
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);


    }


}
