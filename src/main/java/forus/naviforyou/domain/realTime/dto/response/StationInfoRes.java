//package forus.naviforyou.domain.realTime.dto.response;
//
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
//import com.fasterxml.jackson.annotation.JsonInclude;
//import forus.naviforyou.domain.search.dto.response.SearchRes;
//import lombok.Getter;
//import lombok.RequiredArgsConstructor;
//import lombok.Setter;
//
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//import java.util.List;
//
//@Getter
//@Setter
//@RequiredArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
//public class StationInfoRes {
//
//    private ServiceResult ServiceResult;
//
//    @Getter
//    @Setter
//    @RequiredArgsConstructor
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public static class ServiceResult {
//        private MsgBody msgBody;
//
//    }
//
//    @Getter
//    @Setter
//    @RequiredArgsConstructor
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public static class MsgBody{
//
//        @XmlElement(name = "itemList")
//        private List<BusInfoItem> itemList;
//
//    }
//
//    @Getter
//    @Setter
//    @RequiredArgsConstructor
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    @JsonInclude(JsonInclude.Include.NON_NULL)
//    public static class BusInfoItem {
//        private String adirection;
//        private String arrmsg1;
//        private String arrmsg2;
////        private String arrmsgSec1;
////        private String arrmsgSec2;
////        private String arsId;
////        private String busRouteAbrv;
////        private String busRouteId;
////        private String busType1;
////        private String busType2;
////        private String congestion1;
////        private String congestion2;
////        private String deTourAt;
////        private String firstTm;
////        private String gpsX;
////        private String gpsY;
////        private String isArrive1;
////        private String isArrive2;
////        private String isFullFlag1;
////        private String isFullFlag2;
////        private String isLast1;
////        private String isLast2;
////        private String lastTm;
////        private String nextBus;
////        private String nxtStn;
////        private String posX;
////        private String posY;
////        private String repTm1;
////        private String rerdieDiv1;
////        private String rerdieDiv2;
////        private String rerideNum1;
////        private String rerideNum2;
////        private String routeType;
////        private String rtNm;
////        private String sectNm;
////        private String sectOrd1;
////        private String sectOrd2;
////        private String stId;
////        private String stNm;
////        private String staOrd;
////        private String stationNm1;
////        private String stationNm2;
////        private String stationTp;
////        private String term;
////        private String traSpd1;
////        private String traSpd2;
////        private String traTime1;
////        private String traTime2;
////        private String vehId1;
////        private String vehId2;
//    }
//}
