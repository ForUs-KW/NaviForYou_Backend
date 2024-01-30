package forus.naviforyou.global.common.collection.building;

import forus.naviforyou.global.common.collection.enums.Accessibility;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "buildings")
public class Building {

    @Id
    private String id;

    private String roadAddress;

    private Location location ;

    private Map<Accessibility,Boolean> accessibilityList = new HashMap<>(); // 업데이트된 장애물 리스트

    private Map<Accessibility, Integer> userUpdateList = new HashMap<>(); // 유저가 수정한 {장애물,count} 리스트

}

