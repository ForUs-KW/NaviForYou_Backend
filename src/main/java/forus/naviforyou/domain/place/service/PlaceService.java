package forus.naviforyou.domain.place.service;

import forus.naviforyou.domain.place.dto.publicData.BuildingIdDto;
import forus.naviforyou.domain.place.dto.publicData.BuildingAccessibilityListDto;
import forus.naviforyou.domain.place.dto.request.BuildingInfoReq;
import forus.naviforyou.domain.place.dto.request.EditAccessibilityReq;
import forus.naviforyou.domain.place.dto.response.BuildingAccessibilityListRes;
import forus.naviforyou.domain.place.repository.BuildingRepository;
import forus.naviforyou.global.common.Constants;
import forus.naviforyou.global.common.collection.building.Building;
import forus.naviforyou.global.common.collection.enums.Accessibility;
import forus.naviforyou.global.common.service.RedisService;
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

    @Value("${social.publicData.params.serviceKey}")
    private String serviceKey;

    @Value("${social.publicData.path.buildingIdUrl}")
    private String buildingIdUrl;

    @Value("${social.publicData.path.facilityListUrl}")
    private String facilityListUrl;

    public BuildingAccessibilityListRes getBuildingAccessibilityList(BuildingInfoReq req, String member) {
        BuildingAccessibilityListRes res = new BuildingAccessibilityListRes(req.getLocation(), req.getBuildingName());
        String key = Constants.EDIT_FACILITY_FLAG + req.getBuildingName() + member;

        if(redisService.hasKey(key)) {
            res.stringToFacilityList(redisService.getValues(key));
            return res;
        }

        BuildingIdDto managementBuildingId = getBuildingIdApi(req.getBuildingName(), req.getRoadAddress());
        if(managementBuildingId != null){
            getBuildingAccessibilityList(managementBuildingId.getFacilityId(), res);
        }

        buildingRepository.findByLocation(req.getLocation())
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

    private BuildingIdDto getBuildingIdApi(String buildingName, String roadAddress) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(buildingIdUrl)
                .queryParam("serviceKey",serviceKey)
                .queryParam("numOfRows",1)
                .queryParam("faclNm",buildingName)
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
            log.info("apiXml Response = {}",xml);

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
            log.info("Xml BuildingFacilityList = {}",xml);

            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            JAXBContext context = JAXBContext.newInstance(BuildingAccessibilityListDto.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            facilityListDto = (BuildingAccessibilityListDto)unmarshaller.unmarshal(stream);

            log.info("Parsing BuildingFacilityList={} ",facilityListDto);
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
        String key = Constants.EDIT_FACILITY_FLAG + req.getBuildingName() + member;
        if(redisService.hasKey(key)){
            BuildingAccessibilityListRes res = new BuildingAccessibilityListRes(req.getLocation(), req.getRoadAddress());
            res.stringToFacilityList(redisService.getValues(key));
            return res;
        }
        return getBuildingAccessibilityList(req,member);
    }
}
