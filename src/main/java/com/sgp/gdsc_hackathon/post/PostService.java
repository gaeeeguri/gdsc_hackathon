package com.sgp.gdsc_hackathon.post;

import static com.sgp.gdsc_hackathon.global.SecurityUtil.getLoginUsername;

import com.sgp.gdsc_hackathon.post.dto.PostCreateDto;
import com.sgp.gdsc_hackathon.post.dto.PostLinkedResponseDto;
import com.sgp.gdsc_hackathon.post.dto.PostResponseDto;
import com.sgp.gdsc_hackathon.postReceiver.PostReceiverService;
import com.sgp.gdsc_hackathon.postToPost.PostToPostService;
import com.sgp.gdsc_hackathon.user.Member;
import com.sgp.gdsc_hackathon.user.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final MemberService memberService;

    private final PostReceiverService postReceiverService;
    private final PostToPostService postToPostService;

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
        return postRepository.findByMemberId(author);
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

    private Optional<Post> getNextPost(Post curr) {
        return postToPostService.getFrom(curr);
    }

    private List<PostLinkedResponseDto> findLinkedPosts(Post target) {
        Optional<Post> _target = Optional.of(target);
        List<PostLinkedResponseDto> res = new ArrayList<>();

        while (_target.isPresent()) {
            PostLinkedResponseDto cur = PostLinkedResponseDto.builder()
                    .postContent(_target.get().getContent())
                    .postId(_target.get().getId())
                    .build();

            res.add(cur);

            _target = this.getNextPost(_target.get());
        }
        return res;
    }

    public List<List<PostLinkedResponseDto>> findAllLinkedPosts() {

        Member author = memberService.findMember(getLoginUsername());
        List<Post> receivedPosts = postRepository.findByMemberId(author);

        return receivedPosts.stream()
                .map(this::findLinkedPosts)
                .collect(Collectors.toList());
    }
}
