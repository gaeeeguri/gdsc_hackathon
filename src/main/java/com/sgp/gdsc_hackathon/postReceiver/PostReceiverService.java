package com.sgp.gdsc_hackathon.postReceiver;

import com.sgp.gdsc_hackathon.post.Post;
import com.sgp.gdsc_hackathon.user.Member;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostReceiverService {
    private final PostReceiverRepository postReceiverRepository;


    public PostReceiverService(PostReceiverRepository postReceiverRepository) {
        this.postReceiverRepository = postReceiverRepository;
    }


    @Transactional
    public void addReceivers(List<Member> receiverIds, Post post) {
        receiverIds.forEach(receiver -> {
            PostReceiver postReceiver = new PostReceiver();
            postReceiver.setPost(post);
            postReceiver.setMember(receiver);

            postReceiverRepository.save(postReceiver);
            }
        );
    }

    public List<Post> getPostsbyMember(Member member) {
        return postReceiverRepository.findByMember(member);
    }
}
