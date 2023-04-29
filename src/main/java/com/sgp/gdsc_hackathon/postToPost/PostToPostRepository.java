package com.sgp.gdsc_hackathon.postToPost;

import com.sgp.gdsc_hackathon.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostToPostRepository extends JpaRepository<PostToPost, Long> {
    Optional<Post> getFromByTo(Post to);
}
