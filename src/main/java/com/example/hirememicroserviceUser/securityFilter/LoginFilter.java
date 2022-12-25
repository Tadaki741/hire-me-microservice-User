package com.example.hirememicroserviceUser.securityFilter;


import com.example.hirememicroserviceUser.service.AuthenticationService;
import org.springframework.http.HttpStatus;
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
        logger.info(" --> init() method has been get invoked");
        logger.info(" --> Filter name is " + filterConfig.getFilterName());
        logger.info(" --> ServletContext name is" + filterConfig.getServletContext());
        logger.info(" --> init() method is ended");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info(" --> Inside doFilter() of LoginFilter class !!! <--");
        logger.info(" --> doFilter() method is invoked");
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        logger.info(" --> filterChain is working wow");

        //Check for the incoming header if it has the token id
        String headerToken = AuthenticationService.extractTokenStringFromHeader(httpServletRequest);
        if (headerToken != null) {
            //If the request has body
            filterChain.doFilter(httpServletRequest, httpServletResponse); //This will make the program jump into the UserController.java
        }

        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Bearer Token not found !",null);
        }

        logger.info(" --> filterChain is ending wow");
        logger.info(" --> doFilter() method is ended");
    }

    @Override
    public void destroy() {
        logger.info(" --> destroy() method is invoked");
    }


}
