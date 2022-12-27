package com.example.hirememicroserviceUser.repository;

import com.example.hirememicroserviceUser.model.User;

import java.util.Map;

public interface UserRepository {
    void save(User user);
    void saveToCache(User user);

    User findByEmail(String email);
    Map<String, User> findAll();
    void delete(String email);
}
