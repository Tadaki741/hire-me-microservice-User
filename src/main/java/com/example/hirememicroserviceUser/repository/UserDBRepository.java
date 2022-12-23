package com.example.hirememicroserviceUser.repository;

import com.example.hirememicroserviceUser.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDBRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
