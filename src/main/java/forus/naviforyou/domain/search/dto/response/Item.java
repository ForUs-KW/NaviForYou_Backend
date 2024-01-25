package forus.naviforyou.domain.search.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@RequiredArgsConstructor
public class Item {
    @JsonProperty("title")
    private String title;

    @JsonProperty("mapx")
    private String mapx;

    @JsonProperty("mapy")
    private String mapy;

    @JsonProperty("roadAddress")
    private String roadAddress;

    @JsonProperty("category")
    private String category;

    private Double distance;


}