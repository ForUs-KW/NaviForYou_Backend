package forus.naviforyou.domain.findRoute.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TravelRouteRes {

    private MetaData metaData;

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class MetaData {
        private RequestParameters requestParameters;
        private Plan plan;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RequestParameters {
        private int busCount;
        private int expressbusCount;
        private int subwayCount;
//        private int airplaneCount; 항공
//        private String locale;
//        private String endY;
//        private String endX;
        private int wideareaRouteCount;
        private int subwayBusCount;
//        private String startY;
//        private String startX;
//        private int ferryCount; 해운
        private int trainCount;
//        private String reqDttm;
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Plan {
        @JsonProperty("itineraries")
        private List<Itinerary> itineraries;

    }


    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Itinerary {
        private Fare fare;
        private int totalTime;
        private List<Leg> legs;

    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Fare {
        @JsonProperty("regular")
        private RegularFare regular;

    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class RegularFare {
        private int totalFare;

//        private Currency currency;

    }

//    @Getter
//    @Setter
//    @RequiredArgsConstructor
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public static class Currency {
//        @JsonProperty("symbol")
//        private String symbol;
//
//        @JsonProperty("currency")
//        private String currency;
//
//        @JsonProperty("currencyCode")
//        private String currencyCode;
//
//    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Leg {
        private String mode;
        private int sectionTime;
        private int distance;
        private Location start;
        private Location end;
        private List<Step> steps;

    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Location {
        private String name;
        private double lon;
        private double lat;

    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Step {
        private String streetName;
        private int distance;
        private String description;
        private String linestring;

    }
}
