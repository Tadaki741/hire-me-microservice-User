package com.example.hirememicroserviceUser.service;

import com.example.hirememicroserviceUser.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

public class AuthenticationService {

    static final long JWT_EXPIRATION = 864_000_00; // equal 1 day in milliseconds
    static final String JWT_SECRET = "SecretKey";

    static final String PREFIX = "Bearer";

    //Currently this function is not in use
    public static void addToken(HttpServletResponse res, String email){

        String JwtToken = Jwts.builder().setSubject(email)
                //Set the expiration time, add 1 day time
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // changed from HS512 to HS256 makes the application works ? will look more in to this
                .compact();

        res.addHeader("Authorization", PREFIX + " " + JwtToken);

        res.addHeader("Access-Control-Expose-Headers", "Authorization");

    }


    //Function to check if the incoming header has the token or not
    public static String extractTokenStringFromHeader(HttpServletRequest request){
        String requestHeader = request.getHeader("Authorization"); //Authorization is a constant naming convention

        //Split the bearer and the tokenID
        try {
            String[] headerElement = requestHeader.split(" ");

            //Get the token
            return headerElement[1];
        }
        catch (NullPointerException e){
            return null;
        }

    }

    public static String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setClaims(Map.of("email", user.getEmail(), "isRecruiter", user.getIsRecruiter()))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }


}
