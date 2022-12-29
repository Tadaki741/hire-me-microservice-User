package com.example.hirememicroserviceUser.service;

import com.example.hirememicroserviceUser.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@Service
public class AuthenticationService {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationService.class);

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    public void setSecretKey(String secretKey){
        this.secretKey = secretKey;
    }
    static final long JWT_EXPIRATION = 864_000_00; // equal 1 day in milliseconds
    static final String JWT_SECRET = "SecretKey";

    static final String PREFIX = "Bearer";

    //Currently this function is not in use
    public void addToken(HttpServletResponse res, String email){

        String JwtToken = Jwts.builder().setSubject(email)
                //Set the expiration time, add 1 day time
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET) // changed from HS512 to HS256 makes the application works ? will look more in to this
                .compact();

        res.addHeader("Authorization", PREFIX + " " + JwtToken);

        res.addHeader("Access-Control-Expose-Headers", "Authorization");

    }


    //Function to check if the incoming header has the token or not
    public String extractTokenStringFromHeader(HttpServletRequest request){
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

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .setClaims(Map.of("email", user.getEmail(), "isRecruiter", user.getIsRecruiter()))
                .signWith(HS256, this.secretKey)
                .compact();
    }

    public String decodeJWTToken(String token) throws Exception {
        //Extract header and payload in JWT
        String[] jwt = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String header = new String(decoder.decode(jwt[0]));
        String payload = new String(decoder.decode(jwt[1]));

        String tokenWithoutSignature = jwt[0] + "." + jwt[1];
        String signature = jwt[2];

        //Validation part
        SignatureAlgorithm sa = HS256;
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), sa.getJcaName());

        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(sa, secretKeySpec);
        //If token is not validated, return null
        if (!validator.isValid(tokenWithoutSignature, signature)) {
            return null;
        }
        System.out.println(header);
        System.out.println(payload);
        //return header and payload
        return payload;
    }

}
