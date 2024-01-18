package forus.naviforyou.MongoDBTest;

import forus.naviforyou.MongoDBTest.repository.*;
import forus.naviforyou.global.common.entity.*;
import forus.naviforyou.global.common.entity.enums.MemberType;
import forus.naviforyou.global.common.entity.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MongoDbRelationshipTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserCustomizedRepository userCustomizedRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private AccessibilityRepository accessibilityRepository;

    @Autowired
    private CustomizedAccessibilityRepository customizedAccessibilityRepository;

    @Autowired
    private AlterRepository alterRepository;

    @Test
    void testMongoDbRelationship() {
        // Create entities
        Accessibility accessibility = Accessibility.builder().name("Ramp").build();
        Building building = Building.builder().name("Office Building").build();
        CustomizedAccessibility customizedAccessibility = CustomizedAccessibility.builder()
                .accessibility(accessibility)
                .building(building)
                .build();

        Member member = Member.builder()
                .email("test@example.com")
                .phone("123-456-7890")
                .nickname("TestUser")
                .role(Role.ROLE_USER)
                .memberType(MemberType.GENERAL)
                .build();

        UserCustomized userCustomized = UserCustomized.builder()
                .customizedAccessibility(customizedAccessibility)
                .member(member)
                .build();

        Alter alter = Alter.builder()
                .content("Description")
                .imgURL("http://example.com/image.jpg")
                .member(member)
                .customizedAccessibility(customizedAccessibility)
                .build();

        // Save entities
        accessibility = accessibilityRepository.save(accessibility);
        building = buildingRepository.save(building);
        customizedAccessibility = customizedAccessibilityRepository.save(customizedAccessibility);
        member = memberRepository.save(member);
        userCustomized = userCustomizedRepository.save(userCustomized);
        alter = alterRepository.save(alter);

        // Retrieve entities
        Member retrievedMember = memberRepository.findById(member.getId()).orElse(null);
        UserCustomized retrievedUserCustomized = userCustomizedRepository.findById(userCustomized.getId()).orElse(null);
        Alter retrievedAlter = alterRepository.findById(alter.getId()).orElse(null);

        // Assert relationships
        assert retrievedMember != null;
        System.out.println(retrievedUserCustomized.getMember().getEmail());
        assert retrievedUserCustomized != null;
        assert retrievedAlter != null;

//        assert retrievedMember.getUserCustomizedList().contains(retrievedUserCustomized);
//        assert retrievedMember.getAlterList().contains(retrievedAlter);

        assert retrievedUserCustomized.getMember().equals(retrievedMember);
        assert retrievedUserCustomized.getCustomizedAccessibility().equals(customizedAccessibility);

        assert retrievedAlter.getMember().equals(retrievedMember);
        assert retrievedAlter.getCustomizedAccessibility().equals(customizedAccessibility);
    }
}
