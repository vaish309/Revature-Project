//  It interacts with the database using Spring Data JPA. It abstracts SQL querie
package com.revhire.revhire.repository;

import com.revhire.revhire.entity.Certification;
import com.revhire.revhire.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findByUser(User user);
    List<Certification> findByUserId(Long userId);
}
