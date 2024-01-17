package forus.naviforyou.global.common.entity;

import forus.naviforyou.global.common.BaseEntity;
import forus.naviforyou.global.common.entity.enums.MemberType;
import forus.naviforyou.global.common.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String phone;
    private String nickname;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @Column(name = "member_type")
    @Enumerated(value = EnumType.STRING)
    private MemberType memberType;
}
