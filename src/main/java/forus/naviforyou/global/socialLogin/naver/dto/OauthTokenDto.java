package forus.naviforyou.global.socialLogin.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class OauthTokenDto {

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