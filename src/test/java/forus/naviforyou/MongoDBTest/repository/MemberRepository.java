package forus.naviforyou.MongoDBTest.repository;

import forus.naviforyou.global.common.entity.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {
    // 여기에 추가적인 메서드를 선언하여 사용할 수 있습니다.
}
