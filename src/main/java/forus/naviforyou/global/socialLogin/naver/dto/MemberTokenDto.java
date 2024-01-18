package forus.naviforyou.global.socialLogin.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@NoArgsConstructor
public class MemberTokenDto {

    private Long id;
    private String email;
    private String nickname;
    private List<String> roles;

    @Setter
    @JsonProperty("access_token")
    private String accessToken;

    @Setter
    @JsonProperty("refresh_token")
    private String refreshToken;

    private MemberTokenDto(Long id, String email, String nickname, List<String> roles){
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.roles = roles;
    }

//    static public UserTokenDto of(User user) {
//        return new UserTokenDto(user.getId(), user.getEmail(), user.getNickname(), user.getRoles());
//    }

    static public MemberTokenDto of(){
        return new MemberTokenDto();
    }

}
