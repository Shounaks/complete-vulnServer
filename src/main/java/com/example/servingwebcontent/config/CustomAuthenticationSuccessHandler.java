package com.example.servingwebcontent.config;

import com.example.servingwebcontent.repository.Data;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        final String principalRole = ((Data) principal).getRole();
        //BELOW WILL LEAK HASH - Required for INFO
//        log.info(((Data) principal).getPassword());
        if ("ADMIN".equals(principalRole)) {
//            log.warn("ADMIN LOGIN DETECTED: {}", authentication.getName());
            response.sendRedirect("/admin/home");
        } else response.sendRedirect("/home");
    }
}
