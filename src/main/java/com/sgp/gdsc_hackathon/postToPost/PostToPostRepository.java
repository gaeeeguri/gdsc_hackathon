package com.sgp.gdsc_hackathon.postToPost;

import com.sgp.gdsc_hackathon.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostToPostRepository extends JpaRepository<PostToPost, Long> {
    Optional<PostToPost> findByNow(Post nowPost);

    List<PostToPost> findAllByNowIn(List<Post> nowPosts);

    List<PostToPost> findByPrev(Post prevPost);
}
