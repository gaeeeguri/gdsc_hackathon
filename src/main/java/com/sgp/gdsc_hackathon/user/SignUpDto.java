package com.sgp.gdsc_hackathon.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(name = "회원가입 request body")
@Data
@Builder
public class SignUpDto {
    private String username; // ID
    private String password;
    private String nickname;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
