package com.example.hirememicroserviceUser.controller;


import com.example.hirememicroserviceUser.model.LoginBody;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.apache.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {


    @PostMapping
    public String login(@RequestBody LoginBody IdTokenFromUser){
        try {
            String idToken = IdTokenFromUser.getIdToken();
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            String uid = decodedToken.getUid();
            return uid;
        }

        catch (FirebaseAuthException e){
            throw new ResponseStatusException(HttpStatus.SC_UNAUTHORIZED,"Invalid token ID !",e);
        }
    }




}
