package forus.naviforyou.global.common.collection.alter;

import forus.naviforyou.global.common.collection.building.Building;
import forus.naviforyou.global.common.collection.enums.Accessibility;
import forus.naviforyou.global.common.collection.member.Member;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "alters")
public class Alter {

    @Id
    private String id;

    private String writer;

    private String content;

    private String imgURL;

    private Accessibility accessibility;

    @DBRef
    private Building building;

}


