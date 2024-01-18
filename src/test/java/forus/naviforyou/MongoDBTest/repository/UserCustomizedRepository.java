package forus.naviforyou.MongoDBTest.repository;

import forus.naviforyou.global.common.entity.Alter;
import forus.naviforyou.global.common.entity.UserCustomized;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserCustomizedRepository extends MongoRepository<UserCustomized, String> {
    // 여기에 추가적인 메서드를 선언하여 사용할 수 있습니다.
}