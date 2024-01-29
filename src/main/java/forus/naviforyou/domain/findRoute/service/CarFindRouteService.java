package forus.naviforyou.domain.findRoute.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import forus.naviforyou.domain.findRoute.dto.request.CarRouteReq;
import forus.naviforyou.domain.findRoute.dto.response.CarRouteRes;
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
public class CarFindRouteService {
    @Value("${tmap.app-key}")
    private String appKey;

    private RestTemplate restTemplate = new RestTemplate();

    public CarRouteRes getCarRoute(CarRouteReq carRouteReq){


        String routeRes = invokeCarRoute(carRouteReq); // 따옴
        CarRouteRes carRouteRes = parseCarRoute(routeRes); // 파싱

        return carRouteRes;

    }

    public String invokeCarRoute(CarRouteReq carRouteReq){


        // API endpoint URL
        String apiUrl = "https://apis.openapi.sk.com/tmap/routes?version=1";
        String apiKey = appKey;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");
        headers.set("Content-Type", "application/json");
        headers.set("appKey", apiKey);

        HttpEntity<CarRouteReq> requestEntity = new HttpEntity<>(carRouteReq, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        String responseBody = responseEntity.getBody();

        System.out.println("API 응답: " + responseBody);

        return responseBody;
    }


    public CarRouteRes parseCarRoute(String responseBody){


        CarRouteRes carRouteRes = null;

        responseBody = setCoordinates(carRouteRes, responseBody);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            carRouteRes = objectMapper.readValue(responseBody, CarRouteRes.class);
        } catch (Exception e) {
            e.printStackTrace();
//            throw new BaseException(ErrorCode.NO_MAPPING_ROUTE);
        }

        return carRouteRes;

    }

    public String setCoordinates(CarRouteRes carRouteRes, String responseBody) {

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
                        double x = coordinatesNode.get(0).asDouble();
                        double y = coordinatesNode.get(1).asDouble();

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
