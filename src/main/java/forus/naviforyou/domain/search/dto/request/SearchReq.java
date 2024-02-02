package forus.naviforyou.domain.search.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SearchReq {
    //현재 x,y 좌표 (KATECH 좌표계 기준)
    private double x;
    private double y;
}
