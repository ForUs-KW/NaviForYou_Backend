package forus.naviforyou.domain.member.dto.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class NaverResToken {

    @JsonProperty("access_token")
    @Getter
    private String accessToken;

    @JsonProperty("token_type")
    @Getter
    private String tokenType;

    @JsonProperty("refresh_token")
    @Getter
    private String refreshToken;

}