package forus.naviforyou.domain.member.service;

import forus.naviforyou.domain.member.dto.request.SignUpReq;
import forus.naviforyou.domain.member.repository.MemberRepository;
import forus.naviforyou.global.common.entity.Member;
import forus.naviforyou.global.common.entity.enums.MemberType;
import forus.naviforyou.global.common.entity.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void signUp(SignUpReq signUpReq){
        log.info("nickname={}",signUpReq.getNickname());
        memberRepository.save(
                Member.builder()
                        .nickname(signUpReq.getNickname())
                        .email(signUpReq.getEmail())
                        .phone(signUpReq.getPhone())
                        .password(passwordEncoder.encode(signUpReq.getPassword()))
                        .memberType(MemberType.GENERAL)
                        .role(Role.ROLE_USER)
                        .memberType(MemberType.GENERAL)
                        .build()
        );
    }
}
