package forus.naviforyou.domain.place.dto.request;

import forus.naviforyou.global.common.collection.building.Location;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ConvenientFacilityReq {
    private String buildingName;
    private Location location;
    private String roadAddress;

}
