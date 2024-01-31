package forus.naviforyou.domain.place.dto.response;

import forus.naviforyou.domain.place.dto.request.BuildingInfoReq;
import forus.naviforyou.global.common.collection.building.Location;
import lombok.*;

@Setter
@Getter
public class BuildingAccessibilityListRes {

    private Location location;
    private String roadAddress;
    private Boolean elevator;
    private Boolean toilets;
    private Boolean parking;
    private Boolean slide;
    private Boolean bump;


    public BuildingAccessibilityListRes(BuildingInfoReq req) {
        location = req.getLocation();
        roadAddress = req.getRoadAddress();
        elevator = false;
        toilets = false;
        parking = false;
        slide = false;
        bump =false;
    }

    public String facilityListToString(){
        String facilityList = "";
        if(elevator) facilityList += "ELEVATOR ";
        if(toilets) facilityList += "TOILET ";
        if(parking) facilityList += "PARKING ";
        if(slide) facilityList += "SLIDE ";
        if(bump) facilityList += "BUMP ";
        return facilityList;
    }

    public void stringToFacilityList(String facilityList){
        for(String facility : facilityList.split(" ")){
            switch (facility){
                case "ELEVATOR" -> elevator = true;
                case "TOILET" -> toilets = true;
                case "PARKING" -> parking = true;
                case "SLIDE" -> slide = true;
                case "BUMP" -> bump = true;
                default -> {}
            }
        }

    }
}
