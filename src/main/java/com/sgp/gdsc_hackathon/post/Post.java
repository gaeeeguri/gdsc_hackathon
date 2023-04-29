package com.sgp.gdsc_hackathon.post;

import com.sgp.gdsc_hackathon.user.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Post {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;
}
