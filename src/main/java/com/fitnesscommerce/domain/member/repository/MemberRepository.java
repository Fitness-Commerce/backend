package com.fitnesscommerce.domain.member.repository;

import com.fitnesscommerce.domain.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByPhoneNumber(String phoneNumber);
}
