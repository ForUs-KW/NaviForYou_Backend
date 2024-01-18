package forus.naviforyou.global.common.entity;

import forus.naviforyou.global.common.BaseEntity;
import forus.naviforyou.global.common.entity.enums.MemberType;
import forus.naviforyou.global.common.entity.enums.Role;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseEntity {
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

    @BatchSize(size = 100)
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserCustomized> userCustomizedList = new ArrayList<>();

    @BatchSize(size = 100)
    @Builder.Default
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Alter> alterList = new ArrayList<>();
}
