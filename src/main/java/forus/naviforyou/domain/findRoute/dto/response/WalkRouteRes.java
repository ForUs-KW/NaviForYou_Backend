package forus.naviforyou.domain.findRoute.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WalkRouteRes {

//    private String type;
    private List<Feature> features;

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Feature {
//        private String type;
        private Geometry geometry;
        private Properties properties;

    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Geometry {
        private String type;
        private List<List<String>> coordinates;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Properties {
        private Number totalDistance;
        private Number totalTime;
        private Number index;
//        private Number pointIndex;
        private String name;
        private String description;
//        private String direction;
//        private String nearPoiName;
//        private String nearPoiX;
//        private String nearPoiY;
        private String intersectionName;
//        private String facilityType;
        private String facilityName;
        private Number turnType;
        private String pointType;
//        private Number lineIndex;
        private Number distance;
        private Number time;
        private Number roadType;
//        private Number categoryRoadType;

    }

}




