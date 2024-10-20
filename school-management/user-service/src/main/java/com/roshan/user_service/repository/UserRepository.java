package com.roshan.user_service.repository;

import com.roshan.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);


    @Query("SELECT u FROM User u WHERE u.grade = :grade AND u.role = 'ROLE_STUDENT' AND u.deleted = false")
    List<User> findByGrade(Integer grade);
}
