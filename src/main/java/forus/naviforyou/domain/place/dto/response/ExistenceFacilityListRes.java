package forus.naviforyou.domain.place.dto.response;

import forus.naviforyou.domain.place.dto.request.ExistenceFacilityListReq;
import forus.naviforyou.global.common.collection.building.Location;
import lombok.*;

@Setter
@Getter
public class ExistenceFacilityListRes {

    private Location location;
    private String roadAddress;
    private Boolean elevator;
    private Boolean toilets;
    private Boolean parking;
    private Boolean slide;
    private Boolean bump;


    public ExistenceFacilityListRes(ExistenceFacilityListReq req) {
        location = req.getLocation();
        roadAddress = req.getRoadAddress();
        elevator = false;
        toilets = false;
        parking = false;
        slide = false;
        bump =false;
    }
}
