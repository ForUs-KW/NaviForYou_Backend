package forus.naviforyou.domain.place.dto.publicData;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubwayDto {
    private String line;
    private String terminus;
    private String direction;
    private String type;
    private Integer min;
    private Integer sec;

    @JsonCreator
    public SubwayDto(
            @JsonProperty("subwayId") Integer subwayId,
            @JsonProperty("bstatnNm") String terminus,
            @JsonProperty("updnLine") String direction,
            @JsonProperty("btrainSttus") String type,
            @JsonProperty("barvlDt") Integer arrivalTime) {
        this.terminus = terminus;
        this.direction = direction;
        this.type = type;
        this.min = arrivalTime / 60;
        this.sec = arrivalTime % 60;

        switch (subwayId) {
            case 1001, 1002, 1003, 1004, 1005, 1006, 1007, 1008, 1009 -> line = subwayId%1000 + "호선";
            case 1061 -> line = "중앙선";
            case 1063 -> line = "경의중앙선";
            case 1065 -> line = "공항철도";
            case 1067 -> line = "경춘선";
            case 1075 -> line = "수의분당선";
            case 1077 -> line = "신분당선";
            case 1092 -> line = "우이신설선";
            case 1093 -> line = "서해선";
            case 1081-> line = "경강선";
            default -> throw new IllegalStateException("Unexpected value: " + subwayId);
        }
    }

}
