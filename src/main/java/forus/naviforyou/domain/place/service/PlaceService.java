package forus.naviforyou.domain.place.service;

import forus.naviforyou.domain.place.dto.publicData.ManagementFacilityIdRes;
import forus.naviforyou.domain.place.dto.request.ConvenientFacilityReq;
import forus.naviforyou.domain.place.dto.response.ConvenientFacilityRes;
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
        ManagementFacilityIdRes managementBuildingId = getManagementFacilityId(convenientFacilityReq.getBuildingName(), convenientFacilityReq.getRoadAddrName());
        if(managementBuildingId != null){
            getBuildingFacilityList(managementBuildingId.getFacilityId());
        }
        return null;
    }

    private ManagementFacilityIdRes getManagementFacilityId(String buildingName, String roadAddress) {
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(managementFacilityIdUrl)
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
        return parsingManagementFacilityId(result.getBody());
    }

    private URI encodeReservedWord(String uri) {
        uri = uri.contains("%252B") ? uri.replace("%252B", "%2B") : uri;
        uri = uri.contains("%252F") ? uri.replace("%252F", "%2F") : uri;
        return UriComponentsBuilder.fromUriString(uri).build(true).toUri();
    }

    private ManagementFacilityIdRes parsingManagementFacilityId(String apiXml){
        ManagementFacilityIdRes managementFacilityId = null;
        try {
            log.info("apiXml Response = {}",apiXml);

            InputStream stream = new ByteArrayInputStream(apiXml.getBytes());
            JAXBContext context = JAXBContext.newInstance(ManagementFacilityIdRes.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            managementFacilityId = (ManagementFacilityIdRes)unmarshaller.unmarshal(stream);
        }catch (Exception e){
            log.info("parsingManagementFacilityId error: ",e);
        }
        return managementFacilityId;
    }

    private void getBuildingFacilityList(String managementBuildingId){
        UriComponents uriComponents = UriComponentsBuilder
                .fromUriString(conventionFacilityUrl)
                .queryParam("serviceKey",serviceKey)
                .queryParam("wfcltId",managementBuildingId)
                .encode()
                .build();

        URI uri = encodeReservedWord(uriComponents.toUri().toString());
        RestTemplate restTemplate = new RestTemplate();
        RequestEntity<?> req = RequestEntity
                .get(uri)
                .build();
        ResponseEntity<String> result = restTemplate.exchange(req, String.class);

        log.info("getBuildingFacilityList = {}",result);
    }


}
