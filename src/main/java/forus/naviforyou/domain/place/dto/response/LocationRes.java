package forus.naviforyou.domain.place.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationRes {
    private String name;
    private String roadAddress;

    @JsonProperty("addressInfo")
    private void unpackedPoiBuildingInfo(Map<String,Object> addressInfo){
        this.name = (String) addressInfo.get("buildingName");

        this.roadAddress = addressInfo.get("city_do") + " " + addressInfo.get("gu_gun");
        String eupMyun = (String) addressInfo.get("eup_myun");
        if(eupMyun != null && !eupMyun.isEmpty()){
            this.roadAddress += " " + eupMyun;
        }
        this.roadAddress +=  addressInfo.get("roadName") + " " + addressInfo.get("buildingIndex");
    }
}
