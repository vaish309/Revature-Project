package com.revhire.revhire.repository;

import com.revhire.revhire.entity.Resume;
import com.revhire.revhire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByUser(User user);
    Optional<Resume> findByUserId(Long userId);
}
