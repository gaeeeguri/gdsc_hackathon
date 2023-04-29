package com.sgp.gdsc_hackathon.post;


import com.sgp.gdsc_hackathon.user.Member;
import lombok.Getter;

import javax.persistence.*;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@ToString
public class Post {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    // TODO: review ??
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;
    private int depth;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    public void setDepth(int depth) {
        this.depth = depth;
    }




    // TODO: linked list 다음 게시글 확인할 수 있게 id 값 가지기
    public void setMember(Member member) {
        this.member = member;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
