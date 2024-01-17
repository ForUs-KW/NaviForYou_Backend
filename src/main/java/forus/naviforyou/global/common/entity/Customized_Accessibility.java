package forus.naviforyou.global.common.entity;

import forus.naviforyou.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customized_Accessibility extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accessibility_id")
    private Accessibility accessibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @BatchSize(size = 100)
    @Builder.Default
    @OneToMany(mappedBy = "customizedAccessibility", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User_Customized> userCustomizedList = new ArrayList<>();

    @BatchSize(size = 100)
    @Builder.Default
    @OneToMany(mappedBy = "customizedAccessibility", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Alter> alterList = new ArrayList<>();

}
