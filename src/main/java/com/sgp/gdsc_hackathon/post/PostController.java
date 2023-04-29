package com.sgp.gdsc_hackathon.post;

import static com.sgp.gdsc_hackathon.global.SecurityUtil.getLoginUsername;

import com.sgp.gdsc_hackathon.post.dto.MyPostsReqDto;
import com.sgp.gdsc_hackathon.post.dto.OnePostDto;
import com.sgp.gdsc_hackathon.post.dto.PostCreateDto;
import com.sgp.gdsc_hackathon.post.dto.PostLinkedResponseDto;
import com.sgp.gdsc_hackathon.post.dto.PostReceiveDetailResDto;
import com.sgp.gdsc_hackathon.post.dto.PostResponseDto;
import com.sgp.gdsc_hackathon.post.dto.PostsReceiveResDto;
import com.sgp.gdsc_hackathon.postToPost.PostToPostService;
import com.sgp.gdsc_hackathon.security.dto.TokenInfo;
import com.sgp.gdsc_hackathon.user.UserRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: add error handling
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostToPostService postToPostService;

    @GetMapping("/posts")
    @Operation(summary = "내가 쓴 글 조회", description = "로그인한 User가 작성한 게시글들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내가 작성한 글들 조회합니다.", content = @Content(schema = @Schema(implementation = MyPostsReqDto.class))),
            @ApiResponse(responseCode = "406", description = "어딘가 실패했습니다.", content = @Content(schema = @Schema(implementation = UserRes.class)))})
    public MyPostsReqDto getPosts() {
        List<Post> posts = postService.findUserPosts();
        List<OnePostDto> summPosts = new ArrayList<>();
        for (Post post : posts) {
            summPosts.add(OnePostDto.builder()
                    .postId(post.getId())
                    .content(post.getContent())
                    .build());
        }
        return MyPostsReqDto.builder()
                .posts(summPosts)
                .build();
    }


    @PostMapping("/posts")
    @Operation(summary = "글 생성", description = "새 글을 생성해 5명에게 전파한 후, 글의 ID를 반환합니다.")
    public Long createPost(@RequestBody PostCreateDto post) {
        String username = getLoginUsername();
        return postService.upload(post, username);
    }

    @PostMapping("/posts/{from_id}")
    @Operation(summary = "특정 글에 글 추가", description = "{from_id}를 가진 글에 새로운 글을 달고, 5명에게 전파합니다. 반환은 없습니다.")
    public void appendPost(@PathVariable("from_id") Long fromId, @RequestBody PostCreateDto postCreateDto) {
        Long toPostId = this.createPost(postCreateDto);
        Post toPost = postService.getPostById(toPostId);

        Post fromPost = postService.getPostById(fromId);
        postToPostService.addRelation(fromPost, toPost);
    }

    @GetMapping("posts/receive")
    @Operation(summary = "내가 받은 글들 간단 조회", description = "로그인한 User가 받은 글들을 이전 기록 없이 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내게 전송된 글들을 조회합니다.", content = @Content(schema = @Schema(implementation = PostsReceiveResDto.class))),
            @ApiResponse(responseCode = "406", description = "어딘가 실패했습니다.", content = @Content(schema = @Schema(implementation = UserRes.class)))})
    public PostsReceiveResDto getReceivedPosts() {
        List<PostResponseDto> posts = postService.getReceivedPosts();
        return PostsReceiveResDto.builder()
                .posts(posts)
                .build();
    }

    @GetMapping("/posts/all")
    @Operation(summary = "내가 받은 글들 상세 조회", description = "로그인한 User가 받은 글들의 이전 기록(root)까지 전부 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내게 전송된 글들을 조회합니다.", content = @Content(schema = @Schema(implementation = PostReceiveDetailResDto.class))),
            @ApiResponse(responseCode = "406", description = "어딘가 실패했습니다.", content = @Content(schema = @Schema(implementation = UserRes.class)))})
    public PostReceiveDetailResDto getLinkedPosts() {
        List<List<PostLinkedResponseDto>> res = postService.findAllLinkedPosts();
        return PostReceiveDetailResDto.builder()
                .postReceiveDetail(res)
                .build();
    }

//    @GetMapping("/posts/{post_id}")

}
