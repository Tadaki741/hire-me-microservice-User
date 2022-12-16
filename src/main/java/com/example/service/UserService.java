package com.example.service;

import com.example.model.User;
import com.example.repository.UserDBRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;

@Service
public class UserService implements UserRepository {
    private final String USER_CACHE = "USER";

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    UserDBRepository userDBRepository;

    //Define the parameters of operation: USER_CACHE, EMAIL, USER
    private HashOperations<String, String, User> hashOperations;

    // This annotation makes sure that the method needs to be executed after
    // dependency injection is done to perform any initialization.
    @PostConstruct
    private void intializeHashOperations() {
        hashOperations = redisTemplate.opsForHash();
    }

    // Save operation.
    @Override
    public void save(final User user) {

        //Update data to redis
        hashOperations.put("USER_CACHE", user.getId().toString(), user);

        //save to database
        this.userDBRepository.save(user);
    }

    @Override
    public void saveToCache(final User user) {
        hashOperations.put(USER_CACHE, user.getEmail(), user);
    }

    @Override
    public User findByEmail(String email) {
        User redisUser = hashOperations.get(USER_CACHE, email);
        if (redisUser != null) return redisUser;
        else {
            User user = this.getUser(email);
            saveToCache(user);
            return user;
        }
    }

    // Find by employee email operation.
    private User getUser(String email) {
        User user = null;
        try {
            user = this.userDBRepository.findByEmail(email);
            if (user == null) {
                throw new Exception("There is no user with email: " + email);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    // Find all users' operation.
    @Override
    public Map<String, User> findAll() {
        return hashOperations.entries(USER_CACHE);
    }

    // Delete user by email operation.
    @Override
    public void delete(String email) {
        hashOperations.delete(USER_CACHE, email);
    }
}
