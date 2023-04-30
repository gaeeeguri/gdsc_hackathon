package com.sgp.gdsc_hackathon.postToPost;

import com.sgp.gdsc_hackathon.post.Post;
import com.sgp.gdsc_hackathon.postReceiver.PostReceiver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostToPostService {
    private final PostToPostRepository postToPostRepository;


    @Transactional
    public void addRelation(Post fromPost, Post toPost) {
        PostToPost postToPost = PostToPost.builder()
                .prev(fromPost)
                .now(toPost)
                .build();
        log.error("fromPost: {}", fromPost.toString());
        log.error("toPost: {}", toPost.toString());
//        postToPost.setPrev(fromPost);
//        postToPost.setNow(toPost);
        log.error("postToPost: {}", postToPost.toString());
        postToPostRepository.save(postToPost);
    }

    public Post getPrev(Post nowPost) {

        Optional<PostToPost> prevPost = postToPostRepository.findByNow(nowPost);

        if (prevPost.isPresent()) {
            return prevPost.get().getPrev();
        } else {
            return null;
        }
    }

    public Post getByTo(Post now) {
        return postToPostRepository.findByNow(now).get().getPrev();
    }
    public List<Post> getByNows(List<PostReceiver> postReceiver) {
        List<Post> nows = postReceiver.stream().map(p -> p.getPost()).collect(Collectors.toList());
        return postToPostRepository.findAllByNowIn(nows).stream().map(postToPost -> postToPost.getPrev()).collect(Collectors.toList());
    }

    public List<Post> getNowByPrev(Post post) {
        return postToPostRepository.findByPrev(post).stream().map(postToPost -> postToPost.getPrev()).collect(Collectors.toList());
    }
}
