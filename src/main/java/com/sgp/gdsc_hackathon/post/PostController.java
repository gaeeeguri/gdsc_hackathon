package com.sgp.gdsc_hackathon.post;

import com.sgp.gdsc_hackathon.post.dto.PostCreateDto;
import com.sgp.gdsc_hackathon.postToPost.PostToPostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// TODO: add error handling
@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostToPostService postToPostService;

    @GetMapping("/posts/{user_id}")
    @Operation(summary = "Get posts of a user", description = "Returns posts of given user id")
    public Iterable<Post> getPosts(@PathVariable("user_id") Long userId) {
        return postService.findUserPosts(userId);
    }

    @GetMapping("posts/receive/{user_id}")
    public List<Post> getReceivedPosts(@PathVariable("user_id") Long userId) {
        return postService.getReceivedPosts(userId);
    }

    @PostMapping("/posts")
    @Operation(summary = "create post", description = "Create a new post and return id")
    public Long createPost(@RequestBody PostCreateDto post) {
        return postService.upload(post);
    }

    @PostMapping("/posts/{from_id}")
    public void appendPost(@PathVariable("from_id") Long fromId, @RequestBody PostCreateDto postCreateDto) {
        Long toPostId = this.createPost(postCreateDto);
        Post toPost = postService.getPostById(toPostId);

        Post fromPost = postService.getPostById(fromId);
        postToPostService.addRelation(fromPost, toPost);
    }
}
