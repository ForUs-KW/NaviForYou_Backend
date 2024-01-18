package forus.naviforyou.global.common.collection.building;

import forus.naviforyou.global.common.collection.enums.Accessibility;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "buildings")
public class Building {

    @Id
    private String id;

    private String name;

    private String generalAddress;

    private String roadAddress;

    private String detailAddress;

    private Location location ;

    private List<Accessibility> accessibilityList ; // 보유한 장애물 리스트

    private List<UserReport> userReportList ; // 유저가 추가한 {장애물,count} 리스트


}

