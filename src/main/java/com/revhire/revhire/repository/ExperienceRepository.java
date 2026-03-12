package com.revhire.revhire.repository;

import com.revhire.revhire.entity.Experience;
import com.revhire.revhire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findByUser(User user);
    List<Experience> findByUserId(Long userId);
}
