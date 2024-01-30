package forus.naviforyou.domain.place.dto.publicData;

import lombok.*;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "facInfoList")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class BuildingFacilityListDto {

    @XmlElement(name = "servList")
    private FacilityId facilityId;

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "servList")
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @ToString
    protected static class FacilityId{
        @XmlElement(name = "evalInfo")
        private String conventionFacilityList;
    }

    public List<String> getConventionFacilityList(){
        return List.of(facilityId.getConventionFacilityList().split(","));
    }

}
