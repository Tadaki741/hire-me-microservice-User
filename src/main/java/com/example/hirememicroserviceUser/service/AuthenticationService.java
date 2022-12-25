package com.example.hirememicroserviceUser.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

public class AuthenticationService {

    static final long EXPIRATIONTIME = 864_000_00; // equal 1 day in milliseconds
    static final String SIGNINGKEY = "SecretKey";

    static final String PREFIX = "Bearer";

    //Add the information back to the header so that we can send to the front-end
    public static void addToken(HttpServletResponse res, String username){

//these comments code are from Java 17
//        String JwtToken = Jwt.builder().setSubject(username)
//                //Set the expiration time, add 1 day time
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
//                .signWith(SignatureAlgorithm.HS512, SIGNINGKEY) // changed from HS512 to HS256 makes the application works ? will look more in to this
//                .compact();

        String JwtToken = "";

        res.addHeader("Authorization", PREFIX + " " + JwtToken);

        res.addHeader("Access-Control-Expose-Headers", "Authorization");

    }


    //Function to extract the user id from the front-end request
    public static String getAuthentication (HttpServletRequest request) throws FirebaseAuthException {
        String token = request.getHeader("Authorization");// The name "Authorization" is a special name constant


        //If the user has a token
        if(token != null){

            //Extract the data: element 0 = "Bearer" - element 1 = tokenIDString
            String[] dataArray = token.split(" ");
            String userToken = dataArray[1];

            //Use firebase to extract the data from the tokenIDString
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(userToken);

            //If the user has the data
            return decodedToken.getUid();


        }
        return null;
    }


}
