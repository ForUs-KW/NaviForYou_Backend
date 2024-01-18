package forus.naviforyou.global.common.entity;

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
    private Float posX;
    private Float posY;

    @DBRef
    @Builder.Default
    private List<CustomizedAccessibility> customizedAccessibilityList = new ArrayList<>();

}

