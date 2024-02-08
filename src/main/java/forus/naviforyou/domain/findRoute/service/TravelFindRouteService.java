package forus.naviforyou.domain.findRoute.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import forus.naviforyou.domain.findRoute.dto.request.TravelRouteReq;
import forus.naviforyou.domain.findRoute.dto.request.WalkRouteReq;
import forus.naviforyou.domain.findRoute.dto.response.TravelRouteRes;
import forus.naviforyou.domain.findRoute.dto.response.WalkRouteRes;
import forus.naviforyou.domain.realTime.dto.request.BusStationReq;
import forus.naviforyou.domain.realTime.dto.response.ItemList;
import forus.naviforyou.domain.realTime.service.BusService;
import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TravelFindRouteService {
    @Value("${tmap.app-key}")
    private String appKey;

    private final WalkFindRouteService walkFindRouteService;
    private final BusService busService;

    private RestTemplate restTemplate = new RestTemplate();

    public TravelRouteRes getTravelRoute(Boolean disabled , TravelRouteReq travelRouteReq){

        String routeRes = invokeTravelRoute(travelRouteReq); // 따옴
        TravelRouteRes travelRouteRes = parseTravelRoute(routeRes); // 파싱

        if(disabled){
            // 1. 저상버스 필터링
            handleLowBus(travelRouteRes,true);

            // 2. 계단 필터링
            travelRouteRes = handleWalkPart(travelRouteRes,false);
            return travelRouteRes;
        }
        travelRouteRes = handleWalkPart(travelRouteRes,true);

        return  travelRouteRes;
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
                e.printStackTrace();
//                throw new BaseException(ErrorCode.NO_MAPPING_ROUTE);
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

    public TravelRouteRes handleWalkPart(TravelRouteRes travelRouteRes , Boolean includeStairs){

        List<TravelRouteRes.Itinerary> itineraries = travelRouteRes.getMetaData().getPlan().getItineraries();

        try{
            for (TravelRouteRes.Itinerary itinerary : itineraries) {
            List<TravelRouteRes.Leg> legs = itinerary.getLegs();
            for (TravelRouteRes.Leg leg : legs) {
                if (leg.getMode().equals("WALK")){

                    WalkRouteReq walkPartRouteReq = WalkRouteReq.builder()
                         .startX(leg.getStart().getLon())
                         .startY(leg.getStart().getLat())
                         .endX(leg.getEnd().getLon())
                         .endY(leg.getEnd().getLat())
                         .reqCoordType("WGS84GEO")
                         .resCoordType("WGS84GEO")
                         .startName(leg.getStart().getName())
                         .endName(leg.getEnd().getName())
                         .build();

                    if(!includeStairs){
                        walkPartRouteReq.setSearchOption(30);
                    }

                    WalkRouteRes walkPartRouteRes = walkFindRouteService.getWalkRoute(false, walkPartRouteReq);
                    TravelRouteRes.Leg init_leg = new TravelRouteRes.Leg();
                    leg.setSectionTime(init_leg.getSectionTime());
                    leg.setDistance(init_leg.getDistance());
                    leg.setWalkStep(walkPartRouteRes);
                    }
                }
            }
        }catch (Exception e){
            throw new BaseException(ErrorCode.NO_MAPPING_ROUTE_INFO);
        }



        return travelRouteRes;
    }

    public TravelRouteRes handleLowBus(TravelRouteRes travelRouteRes , Boolean lowBus){

        List<TravelRouteRes.Itinerary> itineraries = travelRouteRes.getMetaData().getPlan().getItineraries();
        for (int i = 0; i < itineraries.size(); i++) {
            Boolean includeSubway = false;
            TravelRouteRes.Itinerary itinerary = itineraries.get(i);
            List<TravelRouteRes.Leg> legs = itinerary.getLegs();
            for (TravelRouteRes.Leg leg : legs) {

                if (leg.getMode().equals("SUBWAY")){
                    includeSubway = true;
                }

                if (leg.getMode().equals("BUS")) {
                    BusStationReq busStationReq = new BusStationReq();
                    busStationReq.setX(leg.getStart().getLon());
                    busStationReq.setY(leg.getStart().getLat());
                    String busNum = leg.getRoute();
                    List<ItemList> stationInfoRes = busService.stationInfo(busStationReq, lowBus);
                    if (stationInfoRes == null || stationInfoRes.isEmpty()) {
                        System.out.println(busNum);
                        changeCount(travelRouteRes, includeSubway);
                        itineraries.remove(i);
                        break;
                    }
                    List<ItemList> stationDetailInfoRes = busService.filterBusInfoList(stationInfoRes, busNum);
                    if (stationDetailInfoRes.isEmpty()) {
                        System.out.println(busNum);

                        changeCount(travelRouteRes, includeSubway);
                        itineraries.remove(i);
                        break;
                    }
                }
            }
        }
        return travelRouteRes;
    }

    public void changeCount(TravelRouteRes travelRouteRes , Boolean includeSubway){

        if(includeSubway){
            int currentSubwayBusCount = travelRouteRes.getMetaData().getRequestParameters().getSubwayBusCount();
            travelRouteRes.getMetaData().getRequestParameters().setSubwayBusCount(currentSubwayBusCount-1);

        }
        else {
            int currentBusCount = travelRouteRes.getMetaData().getRequestParameters().getBusCount();
            travelRouteRes.getMetaData().getRequestParameters().setBusCount(currentBusCount-1);
        }
    }
}
