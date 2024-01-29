package forus.naviforyou.domain.place.service;

import forus.naviforyou.domain.place.dto.publicData.BuildingIdDto;
import forus.naviforyou.domain.place.dto.publicData.BuildingFacilityListDto;
import forus.naviforyou.domain.place.dto.request.ConvenientFacilityReq;
import forus.naviforyou.domain.place.dto.response.BuildingFacilityListRes;
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
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    @Value("${social.publicData.params.serviceKey}")
    private String serviceKey;

    @Value("${social.publicData.path.buildingIdUrl}")
    private String buildingIdUrl;

    @Value("${social.publicData.path.facilityListUrl}")
    private String facilityListUrl;

    public BuildingFacilityListRes getConvenientFacility(ConvenientFacilityReq convenientFacilityReq) {
        BuildingFacilityListRes res = new BuildingFacilityListRes(convenientFacilityReq.getBuildingName());
        BuildingIdDto managementBuildingId = getBuildingIdApi(convenientFacilityReq.getBuildingName(), convenientFacilityReq.getRoadAddrName());
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
        Set<String> buildingFacilityListApi = getBuildingFacilityListApi(facilityId);

        for (String facility : buildingFacilityListApi) {
            switch (facility){
                case "승강설비":
                    res.setLiftingFacilities(true);
                    break;
                case "대변기":
                    res.setToilets(true);
                    break;
                case "복도":
                    res.setHallways(true);
                    break;
                case "소변기":
                    res.setUrinals(true);
                    break;
                case "일반사항":
                    res.setGeneralInformation(true);
                    break;
                case "장애인전용주차구역":
                    res.setDisabledParkingArea(true);
                    break;
                case "주출입구 높이차이 제거":
                    res.setNoHeightDifferenceMainEntrance(true);
                    break;
                case "출입구(문)":
                    res.setDoor(true);
                    break;
                case "해당시설 층수":
                    res.setBuildingFloors(true);
                    break;
                default:
                    break;
            }
        }
    }
    private Set<String> getBuildingFacilityListApi(String buildingId){
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

}
