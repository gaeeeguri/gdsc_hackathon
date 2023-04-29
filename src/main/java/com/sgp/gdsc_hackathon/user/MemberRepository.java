package com.sgp.gdsc_hackathon.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long memberId);

    Optional<Member> findByNickname(String nickname);
}
