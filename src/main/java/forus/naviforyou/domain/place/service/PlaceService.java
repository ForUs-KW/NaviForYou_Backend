package forus.naviforyou.domain.place.service;

import forus.naviforyou.domain.place.dto.publicData.BuildingIdDto;
import forus.naviforyou.domain.place.dto.publicData.BuildingFacilityListDto;
import forus.naviforyou.domain.place.dto.request.ConvenientFacilityReq;
import forus.naviforyou.domain.place.dto.request.EditFacilityReq;
import forus.naviforyou.domain.place.dto.response.BuildingFacilityListRes;
import forus.naviforyou.domain.place.repository.BuildingRepository;
import forus.naviforyou.global.common.Constants;
import forus.naviforyou.global.common.collection.building.Building;
import forus.naviforyou.global.common.collection.enums.Accessibility;
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
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    private final BuildingRepository buildingRepository;

    @Value("${social.publicData.params.serviceKey}")
    private String serviceKey;

    @Value("${social.publicData.path.buildingIdUrl}")
    private String buildingIdUrl;

    @Value("${social.publicData.path.facilityListUrl}")
    private String facilityListUrl;

    public BuildingFacilityListRes getConvenientFacility(ConvenientFacilityReq req) {
        BuildingFacilityListRes res = new BuildingFacilityListRes(req);
        BuildingIdDto managementBuildingId = getBuildingIdApi(req.getBuildingName(), req.getRoadAddress());
        if(managementBuildingId != null){
            getBuildingFacilityList(managementBuildingId.getFacilityId(), res);

        }
        return res;
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


    private void getBuildingFacilityList(String facilityId, BuildingFacilityListRes res) {
        List<String> buildingFacilityListApi = getBuildingFacilityListApi(facilityId);

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
    private List<String> getBuildingFacilityListApi(String buildingId){
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
        return parsingBuildingFacilityList(result.getBody()).getConventionFacilityList();
    }

    private BuildingFacilityListDto parsingBuildingFacilityList(String xml){
        BuildingFacilityListDto facilityListDto = null;
        try {
            log.info("Xml BuildingFacilityList = {}",xml);

            InputStream stream = new ByteArrayInputStream(xml.getBytes());
            JAXBContext context = JAXBContext.newInstance(BuildingFacilityListDto.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            facilityListDto = (BuildingFacilityListDto)unmarshaller.unmarshal(stream);

            log.info("Parsing BuildingFacilityList={} ",facilityListDto);
        }catch (Exception e){
            log.info("Parsing BuildingFacilityList error: ",e);
        }
        return facilityListDto;
    }

    public void editConvenientFacility(EditFacilityReq req) {
        Building building = buildingRepository.findByLocation(req.getLocation()).orElse(null);
        if (building == null){
            building = Building.builder()
                    .roadAddress(req.getRoadAddress())
                    .location(req.getLocation())
                    .userUpdateList(new HashMap<>())
                    .accessibilityList(new HashMap<>())
                    .build();
        }

        int flag = req.getEdit() ? 1 : -1;
        int editUserNum = building.getUserUpdateList().getOrDefault(Accessibility.valueOf(req.getFacilityName()),0) + flag;
        if(editUserNum * flag > Constants.EDIT_USER_NUM){
            building.getAccessibilityList().put(Accessibility.valueOf(req.getFacilityName()),req.getEdit());
        }else {
            building.getUserUpdateList().put(Accessibility.valueOf(req.getFacilityName()),editUserNum);
        }



        buildingRepository.save(building);
    }

}
