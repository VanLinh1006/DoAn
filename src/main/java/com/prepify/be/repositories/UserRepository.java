package com.prepify.be.repositories;

import com.prepify.be.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstById(Long id);

    User findFirstByEmail(String name);

    @Query(value = "SELECT u FROM User u WHERE (:email IS NULL OR email like %:email%) AND (:name IS NULL OR name LIKE %:name%)")
    Page<User> findAllWithConditions(@Param("email") String email, @Param("name") String name, Pageable pageable);
}
