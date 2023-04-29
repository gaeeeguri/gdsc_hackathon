package com.sgp.gdsc_hackathon.post;

import com.sgp.gdsc_hackathon.user.User;
import com.sgp.gdsc_hackathon.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long upload(Post post) {
        postRepository.save(post);

        // TODO: add send logic
        return post.getId();
    }


    // TODO: review required
    public Iterable<Post> findUserPosts(Long userId) {
        Optional<User> author = userRepository.findById(userId);
        return postRepository.findByUser(author.get());
    }
}
