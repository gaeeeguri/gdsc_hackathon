package com.sgp.gdsc_hackathon.post.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OnePostDto {
    private Long postId;
    private String content;
}
