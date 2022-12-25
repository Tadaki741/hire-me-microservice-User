package com.example.hirememicroserviceUser.securityFilter;


import com.example.hirememicroserviceUser.service.AuthenticationService;
import com.google.firebase.auth.FirebaseAuthException;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class LoginFilter implements Filter {

    private static final Logger logger = Logger.getLogger(LoginFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("--> init() method has been get invoked");
        logger.info("--> Filter name is " + filterConfig.getFilterName());
        logger.info("--> ServletContext name is" + filterConfig.getServletContext());
        logger.info("--> init() method is ended");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException, IOException {
        logger.info("--> doFilter() method is invoked");

        //Handling incoming request
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        logger.info("--> filterChain function working");
        try {
            //filter request that has a void body
            filterChain.doFilter(httpServletRequest, httpServletResponse);


            //Use the firebase auth to decode the token
            String userDecodedID = AuthenticationService.getAuthentication((HttpServletRequest) servletRequest);

            //If the userDecoded is null, meaning that firebase could not verify that token,
            //Hence the user has provided the incorrect ID
            if (userDecodedID != null){
                logger.info("user's firebase token is not null ! Valid request");
            }

        } catch (ServletException | IOException e) {
            throw new ResponseStatusException(HttpStatus.SC_FORBIDDEN, "The body is void", e);
        } catch (FirebaseAuthException e) {
            throw new ResponseStatusException(HttpStatus.SC_FORBIDDEN, "Invalid token ID", e);
        }

        logger.info("--> filterChain fucntion ended");


        logger.info("--> doFilter() method is ended");
    }

    @Override
    public void destroy() {
        logger.info("--> destroy() method is invoked");
    }


}
