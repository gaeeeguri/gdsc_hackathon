package com.sgp.gdsc_hackathon.post;

import static com.sgp.gdsc_hackathon.global.SecurityUtil.getLoginUsername;

import com.sgp.gdsc_hackathon.post.dto.PostCreateDto;
import com.sgp.gdsc_hackathon.post.dto.PostLinkedResponseDto;
import com.sgp.gdsc_hackathon.post.dto.PostResponseDto;
import com.sgp.gdsc_hackathon.postReceiver.PostReceiverService;
import com.sgp.gdsc_hackathon.postToPost.PostToPostService;
import com.sgp.gdsc_hackathon.user.Member;
import com.sgp.gdsc_hackathon.user.MemberService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
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
    public Long upload(PostCreateDto post, String username, int depth) {
        // TODO: add error handling
        Post newPost = new Post();
        newPost.setContent(post.getContent());

        Member writer = memberService.findMember(username);
        newPost.setMember(writer);
        newPost.setDepth(depth);
        sendPostToRandomUsers(5, newPost);
        postRepository.save(newPost);


        return newPost.getId();
    }


    // TODO: review required
    public List<Post> findUserPosts() {
        String username = getLoginUsername();
        Member author = memberService.findMember(username);

        return postRepository.findAllByMember(author);
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

    private Post getPrevPost(Post curr) {
        return postToPostService.getPrev(curr);
    }

    private List<PostLinkedResponseDto> findLinkedPosts(Post target) {
        Post _target = target;
        List<PostLinkedResponseDto> res = new ArrayList<>();

        while (_target != null) {
            PostLinkedResponseDto cur = PostLinkedResponseDto.builder()
                    .postContent(_target.getContent())
                    .postId(_target.getId())
                    .build();

            res.add(cur);

            _target = this.getPrevPost(_target);
        }

        Collections.reverse(res);
        return res;
    }

    private Boolean notAnsweredPost(Post post) {
        List<Post> answers = postToPostService.getNowByPrev(post);

        for (Post p : answers) {
            if (p.getMember() == memberService.findMember(getLoginUsername())) {
                return false;
            }
        }

        return true;
    }

    public List<List<PostLinkedResponseDto>> findAllLinkedPostsNotAnswered() {

        Member receiver = memberService.findMember(getLoginUsername());
        List<Post> receivedPosts = postReceiverService.getPostsByReceiver(receiver);

        return receivedPosts.stream()
                .filter(this::notAnsweredPost)
                .map(this::findLinkedPosts).collect(Collectors.toList());
    }

    public List<List<PostLinkedResponseDto>> findPublicLinkedPosts() {
        List<Post> satisfiedPosts = postRepository.findAllByDepth(10);

        return satisfiedPosts.stream().map(this::findLinkedPosts).collect(Collectors.toList());
    }

    public List<PostLinkedResponseDto> findSingleLinkedPosts(Long postId) {
        return findLinkedPosts(postRepository.findById(postId).get());
    }
}
