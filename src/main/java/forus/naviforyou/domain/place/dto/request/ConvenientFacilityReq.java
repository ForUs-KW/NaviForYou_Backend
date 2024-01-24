package forus.naviforyou.domain.place.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ConvenientFacilityReq {
    private String buildingName;
    private String siDoName;
    private String cggName;
    private String roadAddrName;

}
