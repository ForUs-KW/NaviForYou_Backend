package forus.naviforyou.MongoDBTest.repository;

import forus.naviforyou.global.common.collection.building.Building;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BuildingRepository extends MongoRepository<Building, String> {
    // 여기에 추가적인 메서드를 선언하여 사용할 수 있습니다.
}
