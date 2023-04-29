package com.sgp.gdsc_hackathon.post.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MyPostsReqDto {
    List<OnePostDto> posts;
}
