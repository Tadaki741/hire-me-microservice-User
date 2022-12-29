package com.example.hirememicroserviceUser.securityFilter;


import com.example.hirememicroserviceUser.service.AuthenticationService;
import lombok.NonNull;
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

    private static final Logger logger = Logger.getLogger(LoginFilter.class.getName());

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        boolean isGetUserEmailPath = path.matches("/users/email/(.*?)");
        boolean isGenerateJWTPath = "/users/auth".equals(path);
        boolean isTestPath = "/users/test".equals(path);
        return isGetUserEmailPath || isGenerateJWTPath || isTestPath;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String headerToken = AuthenticationService.extractTokenStringFromHeader(request);
        if (headerToken == null){
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT");
            return;
        }

        // TODO: decode and validate jwt token here
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        logger.info(" --> destroy() method is invoked");
    }


}
