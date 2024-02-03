package forus.naviforyou.global.common.collection.building;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Location {
    private Float posX;
    private Float posY;
}
