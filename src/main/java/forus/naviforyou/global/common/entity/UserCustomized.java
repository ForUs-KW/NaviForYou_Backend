package forus.naviforyou.global.common.entity;

import forus.naviforyou.global.common.BaseEntity;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("userCustomized")
public class UserCustomized {
    @Id
    private String id;

    @DBRef
    private CustomizedAccessibility customizedAccessibility;

    @DBRef
    private Member member;

}


