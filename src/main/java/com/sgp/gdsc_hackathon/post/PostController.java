package com.sgp.gdsc_hackathon.post;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

// TODO: add error handling
@RestController
public class PostController {
    private final PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/{user_id}")
    @Operation(summary = "Get posts of a user", description = "Returns posts of given user id")
    public Iterable<Post> getPosts(@PathVariable("user_id") Long userId) {
        return postService.findUserPosts(userId);
    }

    @PostMapping("/posts")
    @Operation(summary = "create post", description = "Create a new post and return id")
    public Long createPost(@RequestBody Post post) {
        return postService.upload(post);
    }
}
