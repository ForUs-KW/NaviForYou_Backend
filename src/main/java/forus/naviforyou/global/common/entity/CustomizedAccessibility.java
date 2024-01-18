package forus.naviforyou.global.common.entity;

import forus.naviforyou.global.common.BaseEntity;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomizedAccessibility extends BaseEntity {
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
    @OneToMany(mappedBy = "customizedAccessibility", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserCustomized> userCustomizedList = new ArrayList<>();

    @BatchSize(size = 100)
    @Builder.Default
    @OneToMany(mappedBy = "customizedAccessibility", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Alter> alterList = new ArrayList<>();

}
