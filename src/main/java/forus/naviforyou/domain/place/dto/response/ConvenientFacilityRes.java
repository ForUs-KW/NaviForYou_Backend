package forus.naviforyou.domain.place.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ConvenientFacilityRes {
    private String buildingName;
    private List<String> convenientFacilityList = new ArrayList<>();
}
