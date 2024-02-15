package forus.naviforyou.domain.search.dto.response;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import forus.naviforyou.global.common.collection.building.Location;
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
    public static class Item {
        private final String id;

        private final String name;

        private final String roadAddress; // 도로명 주소

        private final String category; // 분류

        private final String distance; // 거리

        private final Location location;


        @JsonCreator
        public Item(
                @JsonProperty("pkey") String id,
                @JsonProperty("name") String name,
                @JsonSetter("lowerBizName") String category,
                @JsonSetter("radius") String distance,
                @JsonProperty("newAddressList") Map<String,List<Map<String,Object>>> searchPoiInfo,
                @JsonProperty("noorLon") String x,
                @JsonProperty("noorLat") String y
        ){
            this.id = id;
            this.name = name;
            this.category = category;
            this.distance = distance;
            List<Map<String,Object>> newAddress = searchPoiInfo.get("newAddress");
            this.roadAddress = (String) newAddress.get(0).get("fullAddressRoad");
            this.location = Location.builder()
                    .posX(Float.parseFloat(x))
                    .posY(Float.parseFloat(y))
                    .build();
        }
    }
}

