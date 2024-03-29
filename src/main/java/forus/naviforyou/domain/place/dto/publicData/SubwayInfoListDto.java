package forus.naviforyou.domain.place.dto.publicData;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubwayInfoListDto {
    @JsonProperty("realtimeArrivalList")
    private List<SubwayDto> realtimeArrivalList;
}
