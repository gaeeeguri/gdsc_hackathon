package com.sgp.gdsc_hackathon.post;

import static com.sgp.gdsc_hackathon.global.SecurityUtil.getLoginUsername;

import com.sgp.gdsc_hackathon.post.dto.MyPostsReqDto;
import com.sgp.gdsc_hackathon.post.dto.OnePostDto;
import com.sgp.gdsc_hackathon.post.dto.PostCreateDto;
import com.sgp.gdsc_hackathon.post.dto.PostLinkedResDto;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.server.ResponseStatusException;

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
        return postService.upload(post, username, 0);
    }

    @PostMapping("/posts/{from_id}")
    @Operation(summary = "특정 글에 글 추가", description = "{from_id}를 가진 글이 10미만 depth라면 새로운 글을 달고, 5명에게 전파합니다. 반환은 없습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 글에 글을 추가합니다."),
            @ApiResponse(responseCode = "406", description = "글의 depth가 10이라 덧글 추가가 불가합니다.", content = @Content(schema = @Schema(implementation = UserRes.class)))})
    public void appendPost(@PathVariable("from_id") Long fromId, @RequestBody PostCreateDto postCreateDto) {
        String username = getLoginUsername();
        Post prev = postService.getPostById(fromId);
        if (prev.getDepth() == 10) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "글의 depth가 10이라 덧글 작성이 불가합니다.");
        }
        Long toPostId = postService.upload(postCreateDto, username, prev.getDepth() + 1);
        Post toPost = postService.getPostById(toPostId);

        postToPostService.addRelation(prev, toPost);
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
        List<List<PostLinkedResponseDto>> res = postService.findAllLinkedPostsNotAnswered();
        return PostReceiveDetailResDto.builder()
                .postReceiveDetail(res)
                .build();
    }

    @GetMapping("/posts/public")
    @Operation(summary = "답변 10개 넘은 글들(피드글) 조회", description = "User와 상관없이 답변이 딱 10개 달린 글들을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "피드글들을 조회합니다.", content = @Content(schema = @Schema(implementation = PostReceiveDetailResDto.class))),
            @ApiResponse(responseCode = "406", description = "어딘가 실패했습니다.", content = @Content(schema = @Schema(implementation = UserRes.class)))})
    public PostReceiveDetailResDto getPublicPosts() {
        List<List<PostLinkedResponseDto>> res = postService.findPublicLinkedPosts();
        return PostReceiveDetailResDto.builder()
                .postReceiveDetail(res)
                .build();
    }

    @GetMapping("/posts/{post_id}")
    @Operation(summary = "특정 글 상세 조회", description = "User와 상관없이 {post_id}의 글을 상세히 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "특정 글을 상세 조회합니다.", content = @Content(schema = @Schema(implementation = PostReceiveDetailResDto.class))),
            @ApiResponse(responseCode = "406", description = "어딘가 실패했습니다.", content = @Content(schema = @Schema(implementation = UserRes.class)))})
    public PostLinkedResDto getSingleLinkedPosts(@PathVariable("post_id") Long postId) {
        List<PostLinkedResponseDto> res = postService.findSingleLinkedPosts(postId);
        return PostLinkedResDto.builder()
                .postLinked(res)
                .build();
    }
}
