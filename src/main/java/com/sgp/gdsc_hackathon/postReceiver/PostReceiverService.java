package com.sgp.gdsc_hackathon.postReceiver;

import com.sgp.gdsc_hackathon.post.Post;
import com.sgp.gdsc_hackathon.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostReceiverService {
    private final PostReceiverRepository postReceiverRepository;


    public PostReceiverService(PostReceiverRepository postReceiverRepository) {
        this.postReceiverRepository = postReceiverRepository;
    }


    public void addReceivers(List<User> receiverIds, Post post) {
        receiverIds.forEach(receiver -> {
            PostReceiver postReceiver = new PostReceiver();
            postReceiver.setPost(post);
            postReceiver.setUser(receiver);

            postReceiverRepository.save(postReceiver);
            }
        );
    }
}
