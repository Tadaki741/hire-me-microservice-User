package com.example.hirememicroserviceUser.controller;


import com.example.hirememicroserviceUser.HttpResponse.ResponseError;
import com.example.hirememicroserviceUser.model.LoginBody;
import com.example.hirememicroserviceUser.model.User;
import com.example.hirememicroserviceUser.service.AuthenticationService;
import com.example.hirememicroserviceUser.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.hirememicroserviceUser.HttpResponse.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController()
@RequestMapping("users")
@CrossOrigin(origins = "*")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @GetMapping(path = "/test")
    public String test() {
        return "Test from users";
    }

    @PostMapping(path = "/auth")
    public ResponseEntity<ResponseBody> saveNewUserOrLogin(@RequestBody LoginBody loginBody) {
        try {
            //Get the idToken from the login body
            String idToken = loginBody.getIdToken();

            //Decode it
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

            //Get the email
            String userEmail = decodedToken.getEmail();

            //Find the email in database. The library for encode and decode is Apache Commons Code
            //Find the user
            User findingUser = this.userService.findByEmail(userEmail);

            //User not exist in the database
            if (findingUser == null) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User with email " + userEmail + " has not registered!");
            }
            String accessToken = authenticationService.generateToken(findingUser);
            ResponseBody body = new ResponseBody(accessToken);

            //Then we generate the JWT token to send back to the front end
            return new ResponseEntity<>(body, HttpStatus.CREATED);


        } catch (FirebaseAuthException e) {
            //Send back error for invalid IdToken
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid Token ID!", e);
        }
    }

    // Get all users by email
    @GetMapping()
    public ResponseEntity<ResponseBody> findAll() {
        LOG.info("Fetching all users from the redis.");
        final List<User> users = userService.findAll();
        ResponseBody body = new ResponseBody(users);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    // Get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<ResponseBody> findByEmail(@PathVariable("email") final String email) {
        LOG.info("Fetching employee with email = " + email);
        User user = userService.findByEmail(email);

        if (user == null) {
            ResponseBody body = new ResponseBody(null, new ResponseError("User doesn't exist", 404));
            return new ResponseEntity<>(body, HttpStatus.OK);
        }
        ResponseBody body = new ResponseBody(user);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping()
    public ResponseEntity<ResponseBody> insert(@RequestBody User user) {
        User newUser = userService.save(user);
        ResponseBody body = new ResponseBody(newUser);
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    // Delete user by email.
    @DeleteMapping("/delete/{email}")
    public void delete(@PathVariable("email") final String email) {
        LOG.info("Deleting employee with id = " + email);
        // Deleting the user.
        userService.deleteByEmail(email);
    }

    //HANDLING REQUEST FROM MICROSERVICE-CV
    @PostMapping(path = "/verify")
    public ResponseEntity<ResponseBody> verifyTokenRequest(@RequestParam(name = "token") String token){

        try {
            //Decode the token to see if it is correct
            boolean isLegit = authenticationService.isValidToken(token);

            //Prepare body
            ResponseBody responseBody = new ResponseBody(isLegit);

            //return it
            return new ResponseEntity<>(responseBody, HttpStatus.OK);

        }

        catch (Exception e){
            LOG.error(" --> isValidToken produced error");
            return new ResponseEntity<>(null,HttpStatus.CONFLICT);
        }

    }

}
