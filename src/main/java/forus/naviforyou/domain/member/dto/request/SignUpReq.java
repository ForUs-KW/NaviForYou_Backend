package forus.naviforyou.domain.member.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SignUpReq {
    private String nickname;
    private String email;
    private String phone;
    private String password;
}
