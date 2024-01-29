package forus.naviforyou.domain.place.dto.response;

import lombok.*;

@Setter
@Getter
public class BuildingFacilityListRes {
    private String buildingName;
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

    public BuildingFacilityListRes(String name) {
        buildingName = name;
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
