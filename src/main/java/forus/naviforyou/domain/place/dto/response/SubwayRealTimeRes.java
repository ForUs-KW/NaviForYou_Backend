package forus.naviforyou.domain.place.dto.response;

import forus.naviforyou.domain.place.dto.publicData.SubwayDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SubwayRealTimeRes {
    private List<SubwayDto> uphill = new ArrayList<>();
    private List<SubwayDto> downward = new ArrayList<>();

    public void addUphill(SubwayDto subwayDto){
        uphill.add(subwayDto);
    }

    public void addDownward(SubwayDto subwayDto){
        downward.add(subwayDto);
    }
}
