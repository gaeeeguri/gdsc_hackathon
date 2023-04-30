package com.sgp.gdsc_hackathon.postToPost;

import com.sgp.gdsc_hackathon.post.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import lombok.ToString;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
