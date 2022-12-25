package com.example.hirememicroserviceUser.controller;


import com.example.hirememicroserviceUser.model.LoginBody;
import com.example.hirememicroserviceUser.model.User;
import com.example.hirememicroserviceUser.service.AuthenticationService;
import com.example.hirememicroserviceUser.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping
    public String test() {
        return "Test2";
    }


    @PostMapping(path = "user/auth")
    public String saveNewUserOrLogin(@RequestBody LoginBody loginBody) {
        try {
            //Get the idToken from the login body
            String idToken = loginBody.getIdToken();

            //Decode it
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

            //Get the email
            String userEmail = decodedToken.getEmail();

            //Find the email in database. The library for encode and decode is Apache Commons Code
            //If null, send back the JWT contains email + isRecruiter(null)
            //If user has registered, send back the JWT contains email + isRecruiter(user.getRecruiter())

            //Find the user
            User findingUser = this.userService.findByEmail(userEmail);

            if (findingUser == null){//User not exist in the database
                //Initialize it and assign the value
                findingUser = new User(userEmail,false);

                //Save this user to the database
                this.userService.save(findingUser);
            }

            //Then we generate the JWT token to send back to the front end
            return AuthenticationService.generateToken(findingUser);


        } catch (FirebaseAuthException e) {
            //Send back error for invalid IdToken
            throw new ResponseStatusException(HttpStatus.SC_UNAUTHORIZED, "Invalid Token ID!", e);
        }
    }

    // Get all users by email
    @GetMapping("/getall")
    public Map<String, User> findAll() {
        LOG.info("Fetching all users from the redis.");
        final Map<String, User> userMap = (Map<String, User>) userService.findAll();
        return userMap;
    }

    // Get user by email
    @GetMapping("/get/{email}")
    public User findByEmail(@PathVariable("email") final String email) {
        LOG.info("Fetching employee with email = " + email);
        return userService.findByEmail(email);
    }

    // Delete user by email.
    @DeleteMapping("/delete/{email}")
    public Map<String, User> delete(@PathVariable("email") final String email) {
        LOG.info("Deleting employee with id = " + email);
        // Deleting the user.
        userService.deleteByEmail(email);
        // Returning the all users (post the deleted one).
        return findAll();
    }


}
