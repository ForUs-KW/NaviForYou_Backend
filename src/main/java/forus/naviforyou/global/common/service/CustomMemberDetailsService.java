package forus.naviforyou.global.common.service;

import forus.naviforyou.domain.member.repository.MemberRepository;

import forus.naviforyou.global.common.collection.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException(username + " 사용자를 찾을 수 없습니다.")
        );


    }


    private User createUser(Member member) {
        List<GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(member.getRole().toString()));
        return new User(member.getEmail(),
                member.getPassword(),
                grantedAuthorities);
    }
}
