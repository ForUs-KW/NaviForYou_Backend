package forus.naviforyou.domain.place.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import forus.naviforyou.domain.place.dto.publicData.BuildingIdDto;
import forus.naviforyou.domain.place.dto.publicData.BuildingAccessibilityListDto;
import forus.naviforyou.domain.place.dto.publicData.SubwayInfoListDto;
import forus.naviforyou.domain.place.dto.request.BuildingInfoReq;
import forus.naviforyou.domain.place.dto.request.EditAccessibilityReq;
import forus.naviforyou.domain.place.dto.request.LocationReq;
import forus.naviforyou.domain.place.dto.response.BuildingAccessibilityListRes;
import forus.naviforyou.domain.place.dto.response.LocationRes;
import forus.naviforyou.domain.place.dto.response.BuildingInfoRes;
import forus.naviforyou.domain.place.dto.response.SubwayRealTimeRes;
import forus.naviforyou.domain.place.repository.BuildingRepository;
import forus.naviforyou.global.common.Constants;
import forus.naviforyou.global.common.collection.building.Building;
import forus.naviforyou.global.common.collection.enums.Accessibility;
import forus.naviforyou.global.common.service.RedisService;
import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final BuildingRepository buildingRepository;
    private final RedisService redisService;

    @Value("${tmap.app-key}")
    private String tmapServiceKey;

    @Value("${social.publicData.params.serviceKey}")
    private String serviceKey;

    @Value("${social.publicData.path.buildingIdUrl}")
    private String buildingIdUrl;

    @Value("${social.publicData.path.facilityListUrl}")
    private String facilityListUrl;

    public LocationRes convertLocationToAddress(LocationReq req) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://apis.openapi.sk.com/")
                .path("tmap/geo/reversegeocoding")
                .queryParam("version",1)
                .queryParam("lon", req.posX())
                .queryParam("lat",req.posY())
                .queryParam("appKey",tmapServiceKey)
                .queryParam("addressType","A04")
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> apiReq = RequestEntity
                .get(uri)
                .build();

        ResponseEntity<String> result = restTemplate.exchange(apiReq, String.class);

        LocationRes locationRes;
        try {
            locationRes = new ObjectMapper().readValue(result.getBody(), LocationRes.class);
        }
        catch (Exception e) {
            throw new BaseException(ErrorCode.FAILED_CONVERT_LOCATION);
        }

        return locationRes;
    }

    public BuildingInfoRes getBuildingInfo(BuildingInfoReq buildingInfoReq) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://apis.openapi.sk.com/")
                .path("tmap/pois/" + buildingInfoReq.poi())
                .queryParam("version",1)
                .queryParam("findOption","id")
                .queryParam("appKey",tmapServiceKey)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .build();

        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        BuildingInfoRes buildingInfoRes;
        // 원하는 형태로 매핑
        try {
            buildingInfoRes = new ObjectMapper().readValue(result.getBody(), BuildingInfoRes.class);
        }
        catch (Exception e) {
            throw new BaseException(ErrorCode.NO_SUCH_BUILDING);
        }

        return buildingInfoRes;
    }

    public BuildingAccessibilityListRes getBuildingAccessibilityList(BuildingInfoReq req, String member) {
        BuildingAccessibilityListRes res = new BuildingAccessibilityListRes(req.location(), req.roadAddress());
        String key = Constants.EDIT_FACILITY_FLAG + req.buildingName() + member;

        if(redisService.hasKey(key)) {
            res.stringToFacilityList(redisService.getValues(key));
            return res;
        }

        BuildingIdDto managementBuildingId = getBuildingIdApi(req.roadAddress());
        if(managementBuildingId != null){
            getBuildingAccessibilityList(managementBuildingId.getFacilityId(), res);
        }

        buildingRepository.findByLocation(req.location())
                .ifPresent(building -> getBuildingAccessibilityListForDB(building, res));

        redisService.setValues(key, res.facilityListToString(), Duration.ofMinutes(10));
        return res;
    }

    private void getBuildingAccessibilityListForDB(Building building, BuildingAccessibilityListRes res) {
        for (Map.Entry<Accessibility, Boolean> facility : building.getAccessibilityList().entrySet()) {
            Boolean value = facility.getValue();
            switch (facility.getKey()){
                case PARKING -> res.setParking(value);
                case BUMP -> res.setBump(value);
                case ELEVATOR -> res.setElevator(value);
                case SLIDE -> res.setSlide(value);
                case TOILET -> res.setToilets(value);
                default -> {}
            }
        }
    }

    private BuildingIdDto getBuildingIdApi(String roadAddress) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(buildingIdUrl)
                .queryParam("serviceKey",serviceKey)
                .queryParam("numOfRows",1)
                .queryParam("roadNm",roadAddress)
                .encode()
                .build();

        URI uri = encodeReservedWord(uriComponents.toUri().toString());
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .build();
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);
        return parsingBuildingId(result.getBody());
    }

    private URI encodeReservedWord(String uri) {
        uri = uri.contains("%252B") ? uri.replace("%252B", "%2B") : uri;
        uri = uri.contains("%252F") ? uri.replace("%252F", "%2F") : uri;
        return UriComponentsBuilder.fromUriString(uri).build(true).toUri();
    }

    private BuildingIdDto parsingBuildingId(String xml){
        BuildingIdDto buildingIdDto = null;
        try {
            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            JAXBContext context = JAXBContext.newInstance(BuildingIdDto.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            buildingIdDto = (BuildingIdDto)unmarshaller.unmarshal(stream);
        }catch (Exception e){
            log.info("parsingManagementFacilityId error: ",e);
        }
        return buildingIdDto;
    }


    private void getBuildingAccessibilityList(String facilityId, BuildingAccessibilityListRes res) {
        List<String> buildingFacilityListApi = getBuildingAccessibilityListApi(facilityId);

        for (String facility : buildingFacilityListApi) {
            switch (facility) {
                case "승강설비" -> res.setElevator(true);
                case "대변기" -> res.setToilets(true);
                case "장애인전용주차구역" -> res.setParking(true);
                case "주출입구 높이차이 제거" -> res.setBump(true);
                case "주출입구 접근로" -> res.setSlide(true);
                default -> {
                }
            }
        }
    }
    private List<String> getBuildingAccessibilityListApi(String buildingId){
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(facilityListUrl)
                .queryParam("serviceKey",serviceKey)
                .queryParam("wfcltId",buildingId)
                .encode()
                .build();

        URI uri = encodeReservedWord(uriComponents.toUri().toString());
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<?> req = RequestEntity
                .get(uri)
                .build();
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);
        return parsingBuildingAccessibilityList(result.getBody()).getConventionFacilityList();
    }

    private BuildingAccessibilityListDto parsingBuildingAccessibilityList(String xml){
        BuildingAccessibilityListDto facilityListDto = null;
        try {
            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            JAXBContext context = JAXBContext.newInstance(BuildingAccessibilityListDto.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            facilityListDto = (BuildingAccessibilityListDto)unmarshaller.unmarshal(stream);
        }catch (Exception e){
            log.info("Parsing BuildingFacilityList error: ",e);
        }
        return facilityListDto;
    }

    public void editBuildingAccessibility(EditAccessibilityReq req, String member) {
        Building building = buildingRepository.findByLocation(req.location()).orElse(null);
        if (building == null){
            building = Building.builder()
                    .roadAddress(req.roadAddress())
                    .location(req.location())
                    .userUpdateList(new HashMap<>())
                    .accessibilityList(new HashMap<>())
                    .build();
        }

        int flag = req.edit() ? 1 : -1;
        int editUserNum = building.getUserUpdateList().getOrDefault(Accessibility.valueOf(req.facilityName()),0) + flag;
        if(editUserNum * flag > Constants.EDIT_USER_NUM){
            building.getAccessibilityList().put(Accessibility.valueOf(req.facilityName()),req.edit());
            building.getUserUpdateList().remove(Accessibility.valueOf(req.facilityName()));
        }else {
            building.getUserUpdateList().put(Accessibility.valueOf(req.facilityName()),editUserNum);
        }

        buildingRepository.save(building);

        String key = Constants.EDIT_FACILITY_FLAG + req.buildingName() + member;
        if(redisService.hasKey(key)){
            redisService.deleteValues(key);
        }
    }

    public BuildingAccessibilityListRes getBuildingAccessibilityInfoList(BuildingInfoReq req, String member) {
        String key = Constants.EDIT_FACILITY_FLAG + req.buildingName() + member;
        if (redisService.hasKey(key)) {
            BuildingAccessibilityListRes res = new BuildingAccessibilityListRes(req.location(), req.roadAddress());
            res.stringToFacilityList(redisService.getValues(key));
            return res;
        }
        return getBuildingAccessibilityList(req, member);
    }


    public SubwayRealTimeRes getSubwayRealTime(String name, String line) {
        ResponseEntity<String> result = getSubwayApi(name);
        return parsingSubwayApi(line, result);
    }

    private SubwayRealTimeRes parsingSubwayApi(String line, ResponseEntity<String> result) {
        SubwayRealTimeRes subwayRealTimeRes = new SubwayRealTimeRes();

        try {
            SubwayInfoListDto subwayInfoListDto = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(result.getBody(), SubwayInfoListDto.class);

            subwayInfoListDto.getRealtimeArrivalList().forEach(
                    subwayDto -> {
                        if (subwayDto.getLine().equals(line)) {
                            if (subwayDto.getDirection().equals("상행") || subwayDto.getDirection().equals("내선")) {
                                subwayRealTimeRes.addUphill(subwayDto);
                            } else {
                                subwayRealTimeRes.addDownward(subwayDto);
                            }
                        }
                    }
            );
        }
        catch (Exception e) {
            throw new BaseException(ErrorCode.FAILED_REAL_TIME_SUBWAY);
        }

        return subwayRealTimeRes;
    }

    private ResponseEntity<String> getSubwayApi(String name) {
        final String subwayAppKey = "454c6a414c636b6434335752536f4b";
        URI uri = UriComponentsBuilder
                .fromUriString("http://swopenapi.seoul.go.kr")
                .path("/api/subway")
                .path("/" + subwayAppKey)
                .path("/json")
                .path("/realtimeStationArrival")
                .path("/0/100")
                .path("/" + name)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();

        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> apiRes = RequestEntity
                .get(uri)
                .build();

        return restTemplate.exchange(apiRes, String.class);
    }

}
