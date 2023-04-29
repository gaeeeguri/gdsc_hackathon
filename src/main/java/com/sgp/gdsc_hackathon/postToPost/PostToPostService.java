package com.sgp.gdsc_hackathon.postToPost;

import com.sgp.gdsc_hackathon.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostToPostService {
    private final PostToPostRepository postToPostRepository;


    public void addRelation(Post fromPost, Post toPost) {
        PostToPost postToPost = new PostToPost();

        postToPost.setFrom(fromPost);
        postToPost.setTo(toPost);
        postToPostRepository.save(postToPost);
    }

    public Optional<Post> getFrom(Post toPost) {
        return postToPostRepository.getFromByTo(toPost);
    }
}
