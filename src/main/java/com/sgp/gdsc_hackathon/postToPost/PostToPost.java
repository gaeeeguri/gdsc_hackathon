package com.sgp.gdsc_hackathon.postToPost;

import com.sgp.gdsc_hackathon.post.Post;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PostToPost {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prev_id", referencedColumnName = "post_id", updatable = false)
    private Post prev;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "now_id", referencedColumnName = "post_id", updatable = false)
    private Post now;

//    private Long prevId;
//    private Long nowId;
}
