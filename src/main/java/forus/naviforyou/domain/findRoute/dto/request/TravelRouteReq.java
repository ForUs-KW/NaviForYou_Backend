package forus.naviforyou.domain.findRoute.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class TravelRouteReq {
    private String startX;
    private String startY;
    private String endX;
    private String endY;
    private int count;
    private int lang;
    private String format;


}
