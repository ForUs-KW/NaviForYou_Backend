package forus.naviforyou.domain.realTime.dto.request;

import lombok.*;

@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BusStationReq {
    private String x ;
    private String y ;
}
