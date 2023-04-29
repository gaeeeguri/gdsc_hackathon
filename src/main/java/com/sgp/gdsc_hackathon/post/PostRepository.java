package com.sgp.gdsc_hackathon.post;

import com.sgp.gdsc_hackathon.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PostRepository extends CrudRepository<Post, Long> {
    Iterable<Post> findByUser(User user);
}
