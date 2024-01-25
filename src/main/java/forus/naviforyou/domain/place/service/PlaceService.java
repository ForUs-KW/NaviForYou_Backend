package forus.naviforyou.domain.place.service;

import forus.naviforyou.domain.place.dto.request.ConvenientFacilityReq;
import forus.naviforyou.domain.place.dto.response.ConvenientFacilityRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceService {

    @Value("${social.publicData.params.serviceKey}")
    private String serviceKey;

    @Value("${social.publicData.path.managementFacilityIdUrl}")
    private String managementFacilityIdUrl;

    @Value("${social.publicData.path.conventionFacilityUrl}")
    private String conventionFacilityUrl;

    public ConvenientFacilityRes getConvenientFacility(ConvenientFacilityReq convenientFacilityReq) {
        if(getManagementFacilityId(convenientFacilityReq.getBuildingName(), convenientFacilityReq.getRoadAddrName()) != null){

        }
        return null;
    }

    private String getManagementFacilityId(String buildingName, String roadAddress){
        URI uri = UriComponentsBuilder
                .fromUriString(managementFacilityIdUrl)
                .queryParam("numOfRows",1)
                .queryParam("serviceKey",serviceKey)
                .queryParam("faclNm", buildingName)
                .queryParam("roadNm", roadAddress)
                .encode(StandardCharsets.UTF_8)
                .build()
                .toUri();
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<Void> req = RequestEntity
                .get(uri)
                .build();

        ResponseEntity<String> result = restTemplate.exchange(req, String.class);
        log.info("ManagementFacilityId Response = {}",result);
        return "test";
    }


}
