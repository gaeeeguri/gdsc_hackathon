package com.sgp.gdsc_hackathon.post;

import org.springframework.web.bind.annotation.*;

@RestController
public class PostController {
    private final PostService postService;


    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts/{user_id}}")
    public Iterable<Post> getPosts(@PathVariable("user_id") Long userId) {
        return postService.findUserPosts(userId);
    }

    @PostMapping("/posts")
    public Long createPost(@RequestBody Post post) {
        return postService.upload(post);
    }
}
