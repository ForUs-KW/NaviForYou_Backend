package forus.naviforyou.global.common.collection.member;

import forus.naviforyou.global.common.collection.alter.Alter;
import forus.naviforyou.global.common.collection.enums.MemberType;
import forus.naviforyou.global.common.collection.enums.Role;
import lombok.*;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "members")
public class Member {
    @Id
    private String id;

    private String password;
    private String email;
    private String phone;
    private String nickname;

    private Role role;
    private MemberType memberType;

    private UserAccessibility userAccessibility;


    @DBRef
    private List<Alter> alterList ;


}






