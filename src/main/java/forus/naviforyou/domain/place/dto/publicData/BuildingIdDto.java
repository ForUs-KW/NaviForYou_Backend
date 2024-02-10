package forus.naviforyou.domain.place.dto.publicData;


import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "facInfoList")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class BuildingIdDto {

    @XmlElement(name = "totalCount")
    private int totalCount;

    @XmlElement(name = "servList")
    private List<FacilityId> facilityIdList;

    public String getFacilityId(){
        return (totalCount == 0)? null : facilityIdList.get(0).getId();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "servList")
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    protected static class FacilityId{
        @XmlElement(name = "wfcltId")
        private String id;
    }
}
