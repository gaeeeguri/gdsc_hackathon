package com.sgp.gdsc_hackathon.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "로그인 response body")
@Data
public class UserRes {
    String message;
}
