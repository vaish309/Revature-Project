package com.revhire.revhire.repository;

import com.revhire.revhire.entity.Skill;
import com.revhire.revhire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByUser(User user);
    List<Skill> findByUserId(Long userId);
}
