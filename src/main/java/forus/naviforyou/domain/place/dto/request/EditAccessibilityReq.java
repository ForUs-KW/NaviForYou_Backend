package forus.naviforyou.domain.place.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import forus.naviforyou.global.common.collection.building.Location;

public record EditAccessibilityReq(String buildingName, String roadAddress, Location location, String facilityName, Boolean edit) {
    @JsonCreator
    public EditAccessibilityReq(
            @JsonProperty("buildingName") String buildingName,
            @JsonProperty("roadAddress") String roadAddress,
            @JsonProperty("location") Location location,
            @JsonProperty("facilityName") String facilityName,
            @JsonProperty("edit") Boolean edit
    ) {
        this.buildingName = buildingName;
        this.roadAddress = roadAddress;
        this.location = location;
        this.facilityName = facilityName;
        this.edit = edit;
    }
}
