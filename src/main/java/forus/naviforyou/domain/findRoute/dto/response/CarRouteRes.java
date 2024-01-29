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
public class CarRouteRes {

//    private String type;
    private List<Feature> features;


    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Feature {
        private String type;
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
        private List<List<Double>> coordinates;

    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Properties {

        private int totalDistance;
        private int totalTime;
        private int totalFare;
        private int taxiFare;
        private int index;
        private int pointIndex;
        private String name;
        private String description;
        private String nextRoadName;
        private int turnType;
//        private String pointType;
//        private int lineIndex;
        private int distance;
        private int time;
        private int roadType;
        private int facilityType;
    }
}