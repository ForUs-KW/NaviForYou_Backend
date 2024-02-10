package forus.naviforyou.domain.place.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import forus.naviforyou.global.common.collection.building.Location;

public record BuildingInfoReq(String poi, String buildingName, Location location, String roadAddress) {
    @JsonCreator
    public BuildingInfoReq(
            @JsonProperty("poi") String poi,
            @JsonProperty("buildingName") String buildingName,
            @JsonProperty("location") Location location,
            @JsonProperty("roadAddress") String roadAddress) {
        this.buildingName = buildingName;
        this.location = location;
        this.roadAddress = roadAddress;
        this.poi = poi;
    }
}
