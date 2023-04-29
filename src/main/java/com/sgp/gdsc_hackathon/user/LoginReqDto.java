package com.sgp.gdsc_hackathon.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "로그인 request body")
public class LoginReqDto {
    @Schema(description = "ID", example = "ks")
    private String username;

    @Schema(example = "1234")
    private String password;
}
