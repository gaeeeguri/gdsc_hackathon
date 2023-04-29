package com.sgp.gdsc_hackathon.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByTheUserName(String username);
    Optional<User> findByUserName(Optional<String> username);

    Long removeByUserName(String username);
}
