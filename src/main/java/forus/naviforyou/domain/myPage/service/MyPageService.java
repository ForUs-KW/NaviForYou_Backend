package forus.naviforyou.domain.myPage.service;

import forus.naviforyou.domain.member.repository.MemberRepository;
import forus.naviforyou.global.common.collection.member.Member;
import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void delete(String pwd, Member member) {
        if(!passwordEncoder.encode(pwd).equals(member.getPassword())){
            throw new BaseException(ErrorCode.WRONG_PASSWORD);
        }

        memberRepository.delete(member);
    }
}
