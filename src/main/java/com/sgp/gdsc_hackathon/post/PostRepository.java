package com.sgp.gdsc_hackathon.post;

import com.sgp.gdsc_hackathon.user.Member;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByMember(Member author);

    List<Post> findAllByDepth(int depth);
}
