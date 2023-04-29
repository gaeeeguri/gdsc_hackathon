package com.sgp.gdsc_hackathon.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Schema(name = "회원가입 request body")
@Data
@Builder
public class SignUpDto {
    @Schema(description = "ID, 중복되면 안됨", example = "mtk")
    private String username; // ID
    @Schema(description = "비밀번호", example = "1234")
    private String password;
    @Schema(description = "닉네임", example = "mt_kim")
    private String nickname;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .build();
    }
}
