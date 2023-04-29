package com.sgp.gdsc_hackathon.user;

import antlr.Token;
import com.sgp.gdsc_hackathon.security.dto.TokenInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "사용자 인증", description = "회원가입, 로그인")
@RestController
@Slf4j
@RequestMapping(path = "/user", produces = "application/json;charset=UTF-8")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공 : username을 보냅니다.", content = @Content(schema = @Schema(implementation = UserRes.class))),
            @ApiResponse(responseCode = "406", description = "회원가입 실패 : username(ID)는 겹쳐도 되지만, 닉네임은 유일해야합니다.", content = @Content(schema = @Schema(implementation = UserRes.class)))})
    public String signup(@RequestBody SignUpDto signUpDto) {
        String username;
        try {
            username = memberService.signup(signUpDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "회원가입 실패", e);
        }

        return username;
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인을 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공 : username을 보냅니다.", content = @Content(schema = @Schema(implementation = UserRes.class))),
            @ApiResponse(responseCode = "406", description = "로그인 실패 : username(ID)는 겹쳐도 되지만, 닉네임은 유일해야합니다.", content = @Content(schema = @Schema(implementation = UserRes.class)))})
    public TokenInfo login(@RequestBody LoginReqDto loginReqDto) {
        TokenInfo tokeninfo;
        try {
            tokeninfo = memberService.login(loginReqDto.getUsername(), loginReqDto.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "로그인 실패", e);
        }
        return tokeninfo;
    }
}
