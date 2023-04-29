package com.sgp.gdsc_hackathon.postReceiver;

import com.sgp.gdsc_hackathon.post.Post;
import com.sgp.gdsc_hackathon.user.Member;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
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

    public List<Post> getPostsbyMember(Long id) {
        List<Post> ret = new ArrayList<>();
        log.error("멤버 아이디: {}", id);

        List<PostReceiver> postReceivers = postReceiverRepository.findByMemberId(id);
        if (postReceivers.isEmpty()) {
            log.error("이 User가 받은 글이 없습니다.");
            return new ArrayList<>();
        }

        postReceivers.forEach(PostReceiver -> {
                    log.error("포스트 아이디: {}", PostReceiver.getPost().getId());
                    ret.add(PostReceiver.getPost());
                });
        return ret;
    }

    public List<Post> getPostsByReceiver(Member receiver) {
        return postReceiverRepository.findAllByMember(receiver)
                .stream()
                .map(postToPost -> postToPost.getPost())
                .collect(Collectors.toList());
    }
}
