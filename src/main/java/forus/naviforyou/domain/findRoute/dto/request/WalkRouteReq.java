package forus.naviforyou.domain.findRoute.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class WalkRouteReq {
    private String startX;
    private String startY;
    private String endX;
    private String endY;
    private String reqCoordType;
    private String resCoordType;
    private String startName;
    private String endName;
    private int searchOption;
}
