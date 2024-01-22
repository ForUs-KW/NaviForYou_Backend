package forus.naviforyou.domain.member.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CheckCodeReq {
    private String email;
    private String code;
}
