package forus.naviforyou.domain.findRoute.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CarRouteReq {
    private String startX;
    private String startY;
    private String endX;
    private String endY;
//    private String trafficInfo; // 추가 ?
}
