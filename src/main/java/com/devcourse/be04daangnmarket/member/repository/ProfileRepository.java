package com.devcourse.be04daangnmarket.member.repository;

import com.devcourse.be04daangnmarket.member.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByMemberId(Long memberId);

    Optional<Profile> findByUsername(String username);
}
