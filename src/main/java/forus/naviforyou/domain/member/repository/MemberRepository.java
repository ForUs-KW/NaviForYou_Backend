package forus.naviforyou.domain.member.repository;

import forus.naviforyou.global.common.collection.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends MongoRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
}