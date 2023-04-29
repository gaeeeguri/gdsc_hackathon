package com.sgp.gdsc_hackathon.post.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostResponseDto {
    private Long receiverId;
    private Long postId;
    private String postContent;
}
