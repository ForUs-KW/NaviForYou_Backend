package forus.naviforyou.domain.myPage.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeleteReq {
    private String pwd;
}
