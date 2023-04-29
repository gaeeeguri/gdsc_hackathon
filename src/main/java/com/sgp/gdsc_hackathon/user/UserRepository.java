package com.sgp.gdsc_hackathon.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    Optional<User> findByUsername(Optional<String> username);

    Long removeByUsername(String username);
}
