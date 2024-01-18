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
@Document(collection = "accessibilities")
public class Accessibility {

    @Id
    private String id;

    private String name;

    @DBRef
    @Builder.Default
    private List<CustomizedAccessibility> customizedAccessibilityList = new ArrayList<>();

}
