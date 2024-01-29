package forus.naviforyou.domain.findRoute.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import forus.naviforyou.domain.findRoute.dto.request.TravelRouteReq;
import forus.naviforyou.domain.findRoute.dto.response.TravelRouteRes;
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
public class TravelFindRouteService {
    @Value("${tmap.app-key}")
    private String appKey;

    private RestTemplate restTemplate = new RestTemplate();

    public TravelRouteRes getTravelRoute(TravelRouteReq travelRouteReq){
        String routeRes = invokeTravelRoute(travelRouteReq); // 따옴
        TravelRouteRes walkRouteRes = parseTravelRoute(routeRes); // 파싱
        return  walkRouteRes;
    }

    public String invokeTravelRoute(TravelRouteReq travelRouteReq) {

        TravelRouteReq walkRouteRes = null;

        // API endpoint URL
        String apiUrl = "https://apis.openapi.sk.com/transit/routes";
        String apiKey = appKey;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.set("appKey", apiKey);

        HttpEntity<TravelRouteReq> requestEntity = new HttpEntity<>(travelRouteReq, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        String responseBody = responseEntity.getBody();

        return responseBody;
    }
    public TravelRouteRes parseTravelRoute(String responseBody){

        TravelRouteRes walkRouteRes = null;

        if (responseBody.contains("status")){
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);

                int status = jsonNode.get("result").get("status").asInt();
                handleStatus(status);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                walkRouteRes = objectMapper.readValue(responseBody, TravelRouteRes.class);
            } catch (Exception e) {
                throw new BaseException(ErrorCode.NO_MAPPING_ROUTE);
            }
        }
        return walkRouteRes;

    }

    public void handleStatus(int status){
        switch (status) {
            case 11:
                throw new BaseException(ErrorCode.NO_CLOSER_DISTANCE);

            default:
                throw new BaseException(ErrorCode.NO_MAPPING_ROUTE);
        }
    }
}
