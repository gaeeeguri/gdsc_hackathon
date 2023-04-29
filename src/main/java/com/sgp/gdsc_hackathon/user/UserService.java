package com.sgp.gdsc_hackathon.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Iterable<User> findUsers() {
        return userRepository.findAll();
    }

    private void validateDuplication(User user) {
        List<User> findUsers = userRepository.findByUserName(user.getUsername());
        if (!findUsers.isEmpty()) {
            throw new IllegalStateException("User already exists.");
        }
    }

    @Transactional
    public Long join(User user) {
        validateDuplication(user);
        userRepository.save(user);
        return user.getId();
    }
}
