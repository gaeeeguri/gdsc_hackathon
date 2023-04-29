package com.sgp.gdsc_hackathon.post.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class PostLinkedResponseDto {
    private Long postId;
    private String postContent;
}
