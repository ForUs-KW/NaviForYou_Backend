package forus.naviforyou.domain.findRoute.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import forus.naviforyou.domain.findRoute.dto.request.WalkRouteReq;
import forus.naviforyou.domain.findRoute.dto.response.TravelRouteRes;
import forus.naviforyou.domain.findRoute.dto.response.WalkRouteRes;
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
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalkFindRouteService {
    @Value("${tmap.app-key}")
    private String appKey;

    private RestTemplate restTemplate = new RestTemplate();

    public WalkRouteRes getWalkRoute(Boolean includeStairs , WalkRouteReq walkRouteReq){

        walkRouteReq.setReqCoordType("WGS84GEO");
        walkRouteReq.setResCoordType("WGS84GEO");

        if(!includeStairs){
            walkRouteReq.setSearchOption(30);
        }

        WalkRouteRes walkRouteRes = parseWalkRoute(walkRouteReq);
        return  walkRouteRes;
    }

    public WalkRouteRes parseWalkRoute(WalkRouteReq walkRouteReq) {

        WalkRouteRes walkRouteRes = null;

        String apiUrl = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1";
        String apiKey = appKey;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.set("appKey", apiKey);

        HttpEntity<WalkRouteReq> requestEntity = new HttpEntity<>(walkRouteReq, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        String responseBody = responseEntity.getBody();
        responseBody = setCoordinates(walkRouteRes, responseBody); // coordinates 따로 세팅

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            walkRouteRes = objectMapper.readValue(responseBody, WalkRouteRes.class);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.NO_MAPPING_ROUTE);
        }

        return walkRouteRes;
    }

    public String setCoordinates(WalkRouteRes walkRouteRes, String responseBody) {

        String modifiedJsonString = null;

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);

            // features 배열을 순회하면서 type이 "Point"인 경우 coordinates 값에 대괄호 추가
            for (JsonNode featureNode : jsonNode.path("features")) {
                JsonNode geometryNode = featureNode.path("geometry");
                String type = geometryNode.path("type").asText();

                if ("Point".equals(type)) {
                    JsonNode coordinatesNode = geometryNode.path("coordinates");
                    if (coordinatesNode.isArray()) {
                        String x = coordinatesNode.get(0).toString();
                        String y = coordinatesNode.get(1).toString();

                        ((com.fasterxml.jackson.databind.node.ArrayNode) coordinatesNode).removeAll();
                        ((com.fasterxml.jackson.databind.node.ArrayNode) coordinatesNode).add(objectMapper.createArrayNode().add(x).add(y));
                    }
                }
            }

            // 수정된 JsonNode를 문자열로 출력
            modifiedJsonString = objectMapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            throw new BaseException(ErrorCode.NO_MAPPING_ROUTE);
        }

        return modifiedJsonString;

    }
}
