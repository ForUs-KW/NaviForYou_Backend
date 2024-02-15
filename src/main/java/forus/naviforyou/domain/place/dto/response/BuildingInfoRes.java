package forus.naviforyou.domain.place.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import forus.naviforyou.global.common.collection.building.Location;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildingInfoRes {
    private String name;
    private String category;
    private String oldAddress;
    private String roadAddress;
    private String zipCode;
    private String tel;
    private Location location;
    private Boolean park;
    private Boolean twentyFourHour;
    private Boolean allDayOpen;

    private BuildingAccessibilityListRes buildingAccessibilityList;

    @JsonProperty("poiDetailInfo")
    private void unpackedPoiBuildingInfo(Map<String,Object> poiDetailInfo){
        this.name = (String) poiDetailInfo.get("name");
        this.category = (String) poiDetailInfo.get("bizCatName");
        this.zipCode = (String) poiDetailInfo.get("zipCode");
        this.tel = (String) poiDetailInfo.get("tel");

        String parkFlag = (String) poiDetailInfo.get("parkFlag");
        String twFlag = (String) poiDetailInfo.get("twFlag");
        String yaFlag = (String) poiDetailInfo.get("yaFlag");

        this.park = ( parkFlag != null && parkFlag.equals("1"));
        this.twentyFourHour = (twFlag != null && twFlag.equals("1"));
        this.allDayOpen = (yaFlag != null &&  yaFlag.equals("1"));

        this.oldAddress = poiDetailInfo.get("address") + (String) poiDetailInfo.get("firstNo");
        String secondNo = (String) poiDetailInfo.get("secondNo");
        if(secondNo != null && ! secondNo.isEmpty()){
            this.oldAddress += "-" + secondNo;
        }

        this.roadAddress = poiDetailInfo.get("bldAddr") + (String) poiDetailInfo.get("bldNo1");
        String bldNo2 = (String) poiDetailInfo.get("bldNo2");
        if(bldNo2 != null && !bldNo2.isEmpty()){
            this.roadAddress += "-" + bldNo2;
        }

        this.location = Location.builder()
                .posX(Float.parseFloat((String) poiDetailInfo.get("lon")))
                .posY(Float.parseFloat((String) poiDetailInfo.get("lat")))
                .build();
    }

    public void setBuildingAccessibilityList(BuildingAccessibilityListRes buildingAccessibilityList){
        this.buildingAccessibilityList = buildingAccessibilityList;
    }
}
