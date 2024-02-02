package forus.naviforyou.domain.findRoute.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CarRouteReq {
    private double startX;
    private double startY;
    private double endX;
    private double endY;
    private String trafficInfo; // 추가 ?
}
