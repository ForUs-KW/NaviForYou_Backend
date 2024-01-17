package forus.naviforyou.global.common.entity;

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
public class Accessibility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @BatchSize(size = 100)
    @Builder.Default
    @OneToMany(mappedBy = "accessibility", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CustomizedAccessibility> customizedAccessibilityList = new ArrayList<>();
}
