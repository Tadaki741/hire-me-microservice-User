package com.example.hirememicroserviceUser.securityFilter;


import com.example.hirememicroserviceUser.service.AuthenticationService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class LoginFilter extends OncePerRequestFilter {
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    public LoginFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    private AuthenticationService authenticationService;

    private static final Logger logger = Logger.getLogger(LoginFilter.class.getName());

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String method = request.getMethod().toUpperCase();

        boolean isGetUserEmailPath = path.matches("/users/email/(.*?)");
        boolean isGenerateJWTPath = "/users/auth".equals(path);
        boolean isTestPath = "/users/test".equals(path);
        boolean isRegisterUserPath = "/users".equals(path) && (method.equals("POST") || method.equals("OPTIONS"));

        return isGetUserEmailPath || isGenerateJWTPath || isTestPath || isRegisterUserPath;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String headerToken = authenticationService.extractTokenStringFromHeader(request);
        if (headerToken == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT");
            return;
        }

        try {
            String validation = authenticationService.decodeJWTToken(headerToken);
            if (validation == null) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), "JWT NOT VALIDATED");
                return;
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {
        logger.info(" --> destroy() method is invoked");
    }


}
