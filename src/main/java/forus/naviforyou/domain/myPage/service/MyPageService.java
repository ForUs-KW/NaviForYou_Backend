package forus.naviforyou.domain.myPage.service;

import forus.naviforyou.domain.member.repository.MemberRepository;
import forus.naviforyou.global.common.collection.member.Member;
import forus.naviforyou.global.error.dto.ErrorCode;
import forus.naviforyou.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyPageService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void delete(String password, Member member) {
        checkPassword(member,password);

        memberRepository.delete(member);
    }

    public void checkPassword(Member member, String password) {
        if(!passwordEncoder.matches(password, member.getPassword())){
            throw new BaseException(ErrorCode.WRONG_PASSWORD);
        }
    }

    @Transactional
    public void changePwd(String newPwd, Member member) {
       member.setPassword(passwordEncoder.encode(newPwd));

        memberRepository.save(member);
    }
}
