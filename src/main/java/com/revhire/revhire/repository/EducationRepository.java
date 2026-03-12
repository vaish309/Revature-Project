package com.revhire.revhire.repository;

import com.revhire.revhire.entity.Education;
import com.revhire.revhire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByUser(User user);
    List<Education> findByUserId(Long userId);
}
