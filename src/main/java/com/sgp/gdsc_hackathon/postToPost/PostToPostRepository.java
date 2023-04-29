package com.sgp.gdsc_hackathon.postToPost;

import com.sgp.gdsc_hackathon.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostToPostRepository extends JpaRepository<PostToPost, Long> {
    Post getFromByTo(Post to);
}
