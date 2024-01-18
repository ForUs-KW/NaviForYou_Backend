package forus.naviforyou.global.socialLogin.naver.repository;

import forus.naviforyou.global.common.entity.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member,String> {
    Optional<Member> findByEmail(String email);

}
