package forus.naviforyou.domain.search.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchRes {

    private List<Item> items;

    @JsonProperty("searchPoiInfo")
    private void unpackedItems(Map<String,Object> searchPoiInfo){
        Map<String,List<Map<String,Object>>> pois = (Map<String,List<Map<String,Object>>>)searchPoiInfo.get("pois");
        ObjectMapper mapper = new ObjectMapper();
        this.items =  pois.get("poi").stream()
                .map(
                        poi -> mapper.convertValue(poi,Item.class)
                )
                .collect(Collectors.toList());
    }


    @JsonIgnoreProperties(ignoreUnknown = true)
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        @JsonProperty("id")
        private String poiId;

        @JsonProperty("name")
        private String name;

        @JsonProperty("noorLon")
        private String x;

        @JsonProperty("noorLat")
        private String y;

        private String roadAddress; // 도로명 주소

        @JsonProperty("lowerBizName")
        private String lowerBizName; // 분류

        @JsonProperty("radius")
        private String radius; // 분류

        @JsonProperty("newAddressList")
        private void unpackedRoadAddress(Map<String,Object> searchPoiInfo){
            List<Map<String,Object>> newAddress = (List<Map<String, Object>>) searchPoiInfo.get("newAddress");
            this.roadAddress = (String) newAddress.get(0).get("fullAddressRoad");
        }

    }
}
