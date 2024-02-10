package forus.naviforyou.domain.search.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SearchReq {
    private String searchKeyword;
    private int page;
    private int count=10;
    private String searchType="A"; // 거리순 : R
    private String radius="0";
    //현재 x,y 좌표
    private double x;
    private double y;
}
