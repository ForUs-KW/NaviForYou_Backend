package forus.naviforyou.global.common.entity;

import forus.naviforyou.global.common.BaseEntity;
import forus.naviforyou.global.common.entity.enums.MemberType;
import forus.naviforyou.global.common.entity.enums.Role;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

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
@Document("members")
public class Member {
    @Id
    private String id;
    private String email;
    private String phone;
    private String nickname;
    private Role role;
    private MemberType memberType;

    @DBRef
    private List<UserCustomized> userCustomizedList = new ArrayList<>();

    @DBRef
    private List<Alter> alterList = new ArrayList<>();

}


