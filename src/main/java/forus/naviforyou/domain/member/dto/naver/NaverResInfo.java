package forus.naviforyou.domain.member.dto.naver;

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
public class NaverResInfo {

    private String id;
    private String email;
    private String nickname;
    private String phone;

    static public NaverResInfo of(){
        return new NaverResInfo();
    }

    static public NaverResInfo of(String email, String nickname, String phoneNumber, String id) {
        return new NaverResInfo(email, nickname, phoneNumber,id);
    }

}
