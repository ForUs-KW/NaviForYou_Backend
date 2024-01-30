package forus.naviforyou.domain.place.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import forus.naviforyou.global.common.collection.building.Location;
import lombok.*;

@Getter
public class EditFacilityReq {
    private final String roadAddress;
    private final Location location;
    private final String facilityName;
    private final Boolean edit;

    @JsonCreator
    public EditFacilityReq(
            @JsonProperty("roadAddress") String roadAddress,
            @JsonProperty("location") Location location,
            @JsonProperty("facilityName") String facilityName,
            @JsonProperty("edit") Boolean edit
    ) {
        this.roadAddress = roadAddress;
        this.location = location;
        this.facilityName = facilityName;
        this.edit = edit;
    }
}
