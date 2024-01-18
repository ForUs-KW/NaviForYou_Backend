package forus.naviforyou.global.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "customized_accessibility")
public class CustomizedAccessibility {

    @Id
    private String id;

    private Accessibility accessibility;
    private Building building;

    @DBRef
    @Builder.Default
    private List<UserCustomized> userCustomizedList = new ArrayList<>();

    @DBRef
    @Builder.Default
    private List<Alter> alterList = new ArrayList<>();

}

