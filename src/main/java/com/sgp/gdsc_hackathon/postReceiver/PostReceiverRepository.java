package com.sgp.gdsc_hackathon.postReceiver;

import com.sgp.gdsc_hackathon.post.Post;
import com.sgp.gdsc_hackathon.user.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostReceiverRepository extends JpaRepository<PostReceiver, Long> {
    List<PostReceiver> findByMemberId(Long id);

    List<PostReceiver> findAllByMember(Member receiver);

    void deleteByMemberIdAndPostId(Long memberId, Long postId);
}
