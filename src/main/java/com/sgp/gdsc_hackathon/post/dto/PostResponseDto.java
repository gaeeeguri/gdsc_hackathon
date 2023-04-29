package com.sgp.gdsc_hackathon.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostResponseDto {
    @Schema(description = "로그인한 user의 DB의 id", example = "1")
    private Long receiverId;
    private Long postId;
    private String postContent;
}
