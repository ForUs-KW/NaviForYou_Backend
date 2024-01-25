package forus.naviforyou.domain.member.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChangePwdReq {
    private String email;
    private String newPassword;
}
