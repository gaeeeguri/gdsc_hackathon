package com.sgp.gdsc_hackathon.post;

import static com.sgp.gdsc_hackathon.global.SecurityUtil.getLoginUsername;

import com.sgp.gdsc_hackathon.post.dto.PostCreateDto;
import com.sgp.gdsc_hackathon.post.dto.PostResponseDto;
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

        users.remove(memberService.findMember(getLoginUsername()));

        while (receivers.size() <= n) {
            if (users.size() == 0) {
                break;
            }

            int index = random.nextInt(users.size());

            receivers.add(users.get(index));
            users.remove(index);
        }

        postReceiverService.addReceivers(receivers, post);
    }

    @Transactional
    public Long upload(PostCreateDto post, String username) {
        // TODO: add error handling
        Post newPost = new Post();
        newPost.setContent(post.getContent());

        Member writer = memberService.findMember(username);
        newPost.setMember(writer);
        sendPostToRandomUsers(5, newPost);
        postRepository.save(newPost);


        return newPost.getId();
    }


    // TODO: review required
    public List<Post> findUserPosts() {
        String username = getLoginUsername();
        Member author = memberService.findMember(username);
        return postRepository.findByMemberId(author.getId());
    }

    public List<PostResponseDto> getReceivedPosts() {
        String username = getLoginUsername();
        Member member = memberService.findMember(username);
        List<Post> posts =  postReceiverService.getPostsbyMember(member.getId());

        List<PostResponseDto> res = new ArrayList<>();

        posts.forEach(post -> {
            PostResponseDto dto = PostResponseDto.builder()
                    .postContent(post.getContent())
                    .receiverId(member.getId())
                    .postId(post.getId())
                    .build();
            res.add(dto);
        });
        return res;
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId).get();
    }
}
