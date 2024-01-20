package forus.naviforyou.domain.socialLogin.naver.dto;

import lombok.*;

//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MemberInfoOauthDto {

    private String email;
    private String nickname;
    private String phone;

    private MemberInfoOauthDto(String email, String nickname){
        this.email = email;
        this.nickname = nickname;
    }

//    public User toEntity() {
//        return User.of(
//                this.email, this.nickname, this.phoneNumber
//        );
//    }

    static public MemberInfoOauthDto of(){
        return new MemberInfoOauthDto();
    }

    static public MemberInfoOauthDto of(String email, String nickname, String phoneNumber) {
        return new MemberInfoOauthDto(email, nickname, phoneNumber);
    }

//    static public UserInfoOauthDto of(User user) {
//        return new UserInfoOauthDto(user.getEmail(), user.getNickname(), user.getPhoneNumber());
//    }

    static public MemberInfoOauthDto of(String email, String nickname) {
        return new MemberInfoOauthDto(email, nickname);
    }
}
