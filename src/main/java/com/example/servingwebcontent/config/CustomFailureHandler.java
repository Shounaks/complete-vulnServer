package com.example.servingwebcontent.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.Logger;

import static com.example.servingwebcontent.utils.LogUtils.logging;

//@Slf4j
@Component
//@RequiredArgsConstructor
public class CustomFailureHandler implements AuthenticationFailureHandler {
    Logger logger = Logger.getLogger(CustomFailureHandler.class.getName());

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logging(logger, exception.getMessage());
        response.sendRedirect("/login?error=true&errorMessage=" + exception.getMessage());
    }
}