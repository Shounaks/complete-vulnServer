package com.example.servingwebcontent.config;

import com.example.servingwebcontent.repository.Data;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        final String principalRole = ((Data) principal).getRole();
        if ("ADMIN".equals(principalRole)) {
            response.sendRedirect("/admin/home");
        } else response.sendRedirect("/home");
    }
}
