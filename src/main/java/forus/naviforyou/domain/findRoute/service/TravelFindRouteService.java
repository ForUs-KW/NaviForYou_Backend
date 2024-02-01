package forus.naviforyou.domain.findRoute.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import forus.naviforyou.domain.findRoute.dto.request.TravelRouteReq;
import forus.naviforyou.domain.findRoute.dto.request.WalkRouteReq;
import forus.naviforyou.domain.findRoute.dto.response.TravelRouteRes;
import forus.naviforyou.domain.findRoute.dto.response.WalkRouteRes;
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
    private final WalkFindRouteService walkFindRouteService;

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

    public String handleWalkPart(WalkRouteRes walkRouteRes, String responseBody){

        String modifiedJsonString = null;

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // legs 배열을 순회하면서 type이 "Point"인 경우 coordinates 값에 대괄호 추가
            for (JsonNode legsNode : jsonNode.path("metadata").path("plan").path("iteraries").path("legs")) {
                String mode = legsNode.path("mode").asText();

                if ("WALK".equals(mode)) {

                    WalkRouteReq walkPartRouteReq = WalkRouteReq.builder()
                            .startX(legsNode.path("start").path("lon").asText())
                            .startY(legsNode.path("start").path("lat").asText())
                            .endX(legsNode.path("end").path("lon").asText())
                            .endY(legsNode.path("end").path("lon").asText())
                            .reqCoordType("WGS84GEO")
                            .resCoordType("EPSG3857")
                            .startName(legsNode.path("start").path("name").asText())
                            .endName(legsNode.path("end").path("name").asText())
                            .build();

                    WalkRouteRes walkPartRouteRes = walkFindRouteService.getWalkRoute(false , walkPartRouteReq);




//                    legsNode.
//                    JsonNode coordinatesNode = geometryNode.path("coordinates");
//                    if (coordinatesNode.isArray()) {
//                        double x = coordinatesNode.get(0).asDouble();
//                        double y = coordinatesNode.get(1).asDouble();
//
//                        ((com.fasterxml.jackson.databind.node.ArrayNode) coordinatesNode).removeAll();
//                        ((com.fasterxml.jackson.databind.node.ArrayNode) coordinatesNode).add(objectMapper.createArrayNode().add(x).add(y));
//                    }
                }
            }

            // 수정된 JsonNode를 문자열로 출력
            modifiedJsonString = objectMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.NO_MAPPING_ROUTE);
        }



        return "작업중";
    }
}
