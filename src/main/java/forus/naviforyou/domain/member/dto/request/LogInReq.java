package forus.naviforyou.domain.member.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LogInReq {
    private String email;
    private String password;
}
