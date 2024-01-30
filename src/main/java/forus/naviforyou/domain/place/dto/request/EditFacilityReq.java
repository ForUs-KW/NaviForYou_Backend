package forus.naviforyou.domain.place.dto.request;

import forus.naviforyou.global.common.collection.building.Location;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EditFacilityReq {
    private String roadAddress;
    private Location location;
    private String facilityName;
    private Boolean edit;
}
