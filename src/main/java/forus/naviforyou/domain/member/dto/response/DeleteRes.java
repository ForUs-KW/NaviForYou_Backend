package forus.naviforyou.domain.member.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeleteRes {
    private Boolean result;
}
