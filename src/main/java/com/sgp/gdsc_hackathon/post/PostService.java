package com.sgp.gdsc_hackathon.post;

import com.sgp.gdsc_hackathon.postReceiver.PostReceiverService;
import com.sgp.gdsc_hackathon.user.Member;
import com.sgp.gdsc_hackathon.user.MemberService;
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
    private final MemberService memberService;

    private final PostReceiverService postReceiverService;

    private void sendPostToRandomUsers(int n, Post post) {
        Random random = new Random();
        List<Member> users = memberService.findMembers();

        List<Member> receivers = new ArrayList<Member>();

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
    public List<Post> findUserPosts(Long userId) {
        Member author = memberService.findMember(userId);
        return postRepository.findByMemberId(author);
    }

    public List<Post> getReceivedPosts(Long userId) {
        Member member = memberService.findMember(userId);
        return postReceiverService.getPostsbyMember(member);
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).get();
    }
}
