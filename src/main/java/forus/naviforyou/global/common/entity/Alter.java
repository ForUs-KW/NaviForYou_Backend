package forus.naviforyou.global.common.entity;

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

    private String content;

    @Column(name = "img_URL")
    private String imgURL;

    @DBRef
    private Member member;

    @DBRef
    private CustomizedAccessibility customizedAccessibility;
}


