package com.example.hirememicroserviceUser.repository;

import com.example.hirememicroserviceUser.model.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDBRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    @Override
    List<User> findAll();

    User deleteByEmail(String email);
}
