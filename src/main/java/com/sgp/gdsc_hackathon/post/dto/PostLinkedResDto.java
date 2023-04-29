package com.sgp.gdsc_hackathon.post.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostLinkedResDto {
    List<PostLinkedResponseDto> postLinked;
}
