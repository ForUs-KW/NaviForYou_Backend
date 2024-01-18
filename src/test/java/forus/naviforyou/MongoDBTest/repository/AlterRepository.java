package forus.naviforyou.MongoDBTest.repository;

import forus.naviforyou.global.common.collection.alter.Alter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlterRepository extends MongoRepository<Alter, String> {
    // 여기에 추가적인 메서드를 선언하여 사용할 수 있습니다.
}
