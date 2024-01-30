package forus.naviforyou.domain.place.dto.response;

import forus.naviforyou.domain.place.dto.request.ConvenientFacilityReq;
import forus.naviforyou.global.common.collection.building.Location;
import lombok.*;

@Setter
@Getter
public class BuildingFacilityListRes {

    private Location location;
    private String roadAddress;
    private Boolean elevator;
    private Boolean toilets;
    private Boolean parking;
    private Boolean slide;
    private Boolean bump;

    /**
    private Boolean buildingFloors; //건물 층수
    private Boolean hallways; //복도
    private Boolean urinals;  //소변기
    private Boolean generalInformation; //일반 정보
    private Boolean door; // 문
    */

    public BuildingFacilityListRes(ConvenientFacilityReq req) {
        location = req.getLocation();
        roadAddress = req.getRoadAddress();
        elevator = false;
        toilets = false;
        parking = false;
        slide = false;
        bump =false;
    }
}
