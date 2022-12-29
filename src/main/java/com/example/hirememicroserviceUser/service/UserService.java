package com.example.hirememicroserviceUser.service;

import com.example.hirememicroserviceUser.model.User;
import com.example.hirememicroserviceUser.repository.UserDBRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class UserService {
    private final String USER_CACHE = "USER";
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserDBRepository userDBRepository;

    @Autowired
    public UserService(UserDBRepository userDBRepository, RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
        this.userDBRepository = userDBRepository;

    }

    //Define the parameters of operation: USER_CACHE, EMAIL, USER
    private HashOperations<String, String, User> hashOperations;

    // This annotation makes sure that the method needs to be executed after
    // dependency injection is done to perform any initialization.
    @PostConstruct
    private void initializeHashOperations() {
        hashOperations = redisTemplate.opsForHash();
    }

    // Save operation.
    public User save(final User user) {
        User newUser = this.userDBRepository.save(user);
        //Update data to redis
        this.saveUserToCache(newUser);

        //save to database
        return newUser;
    }

    public void saveUserToCache(final User user) {
        hashOperations.put(USER_CACHE, user.getEmail(), user);
    }

    public User findByEmail(String email) {
        User redisUser = hashOperations.get(USER_CACHE, email);
        if (redisUser != null) return redisUser;
        User user = this.userDBRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        return user;
    }

    // Find all users' operation.
    public List<User> findAll() {
        List<User> users = hashOperations.values(USER_CACHE);

        if (!users.isEmpty()){
            return users;
        }

        return this.userDBRepository.findAll();
    }

    // Delete user by email operation.
    public void deleteByEmail(String email) {
        hashOperations.delete(USER_CACHE, email);
        this.userDBRepository.deleteByEmail(email);
    }
}
