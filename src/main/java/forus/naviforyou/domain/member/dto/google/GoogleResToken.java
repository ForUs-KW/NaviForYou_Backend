package forus.naviforyou.domain.member.dto.google;

import lombok.Data;

@Data
public class GoogleResToken {
    private String access_token;
    private String expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;
    private String id_token;
}
