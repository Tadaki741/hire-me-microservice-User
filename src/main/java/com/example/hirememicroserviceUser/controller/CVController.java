package com.example.hirememicroserviceUser.controller;


import com.example.hirememicroserviceUser.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

import com.example.hirememicroserviceUser.HttpResponse.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * This class will handle incoming request from the microservice of CV
 */

@RestController
@RequestMapping(path = "CV")
@CrossOrigin(origins = "*")
public class CVController {

    private static final Logger logger = Logger.getLogger(CVController.class.getName());

    @Autowired
    private AuthenticationService authenticationService;

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
            logger.severe(" --> isValidToken produced error");
            return new ResponseEntity<>(null,HttpStatus.CONFLICT);
        }

    }

}
