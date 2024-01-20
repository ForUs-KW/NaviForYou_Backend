package forus.naviforyou.domain.member.dto.kakao;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class KakaoSignUp {
    private String nickname;
    private String email;
    private String password;
}
