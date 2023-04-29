package com.sgp.gdsc_hackathon.post;

import com.sgp.gdsc_hackathon.postReceiver.PostReceiverService;
import com.sgp.gdsc_hackathon.user.User;
import com.sgp.gdsc_hackathon.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    private final PostReceiverService postReceiverService;

    private void sendPostToRandomUsers(int n, Post post) {
        Random random = new Random();
        List<User> users = userService.findUsers();

        List<User> receivers = new ArrayList<User>();

        for (int i = 0; i < n; i++) {
            int index = random.nextInt(users.size());
            receivers.add(users.get(index));
            users.remove(index);
        }

        postReceiverService.addReceivers(receivers, post);
    }

    @Transactional
    public Long upload(Post post) {
        // TODO: add error handling
        sendPostToRandomUsers(5, post);

        postRepository.save(post);

        return post.getId();
    }


    // TODO: review required
    public Iterable<Post> findUserPosts(Long userId) {
        User author = userService.findUser(userId);
        return postRepository.findByUser(author);
    }

    public List<Post> getReceivedPosts(Long userId) {
        User user = userService.findUser(userId);
        return postReceiverService.getPostsbyUser(user);
    }
}
