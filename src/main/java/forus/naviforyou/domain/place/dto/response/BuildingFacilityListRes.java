package forus.naviforyou.domain.place.dto.response;

import forus.naviforyou.domain.place.dto.request.ConvenientFacilityReq;
import forus.naviforyou.global.common.collection.building.Location;
import lombok.*;

@Setter
@Getter
public class BuildingFacilityListRes {

    private Location location;
    private String roadAddress;
    private Boolean liftingFacilities;
    private Boolean toilets;
    private Boolean hallways;
    private Boolean urinals;
    private Boolean generalInformation;
    private Boolean disabledParkingArea;
    private Boolean mainEntranceAccessRoad;
    private Boolean noHeightDifferenceMainEntrance;
    private Boolean door;
    private Boolean buildingFloors;

    public BuildingFacilityListRes(ConvenientFacilityReq req) {
        location = req.getLocation();
        roadAddress = req.getRoadAddress();
        liftingFacilities = false;
        toilets = false;
        hallways = false;
        urinals = false;
        generalInformation = false;
        disabledParkingArea = false;
        mainEntranceAccessRoad = false;
        door = false;
        buildingFloors  = false;
        noHeightDifferenceMainEntrance =false;
    }
}
