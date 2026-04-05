package com.andre.spring_react_project.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.andre.spring_react_project.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    UserEntity findByEmail(String email);
    UserEntity findById(long id);


    // We use a custom SQL query to better select what we want (find all usernames with the given prefix)
    // Also, we use pageable to limit the number of usernames displayed
    @Query("SELECT u.username FROM UserEntity u WHERE u.username LIKE :username%")
    List<String> findUsernamesStartingWith(@Param("username") String username, Pageable pageable);
    
}
