package forus.naviforyou.domain.place.dto.tmap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PoiBuildingInfo {
    private String name;
    private String bizCatName;
    private String address;
    private String firstNo;
    private String secondNo;
    private String zipCode;
    private String tel;
    private String bldAddr;
    private String bldNo1;
    private String bldNo2;
    private Float lat;
    private Float lon;
    private Integer parkFlag;
    private Integer twFlag;
    private Integer yaFlag;

}
