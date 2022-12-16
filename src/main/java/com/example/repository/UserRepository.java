package com.example.repository;

import com.example.model.User;

import java.util.Map;

public interface UserRepository {
    void save(User user);
    void saveToCache(User user);

    User findByEmail(String email);
    Map<String, User> findAll();
    void delete(String email);
}
