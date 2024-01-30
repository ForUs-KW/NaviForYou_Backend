package forus.naviforyou.domain.place.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import forus.naviforyou.global.common.collection.building.Location;
import lombok.*;

@Getter
public class ExistenceFacilityListReq {
    private final String buildingName;
    private final Location location;
    private final String roadAddress;

    @JsonCreator
    public ExistenceFacilityListReq(
            @JsonProperty("buildingName") String buildingName,
            @JsonProperty("location") Location location,
            @JsonProperty("roadAddress") String roadAddress) {
        this.buildingName = buildingName;
        this.location = location;
        this.roadAddress = roadAddress;
    }
}
