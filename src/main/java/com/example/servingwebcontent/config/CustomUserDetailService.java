package com.example.servingwebcontent.config;

import com.example.servingwebcontent.DataRepository;
import com.example.servingwebcontent.repository.Data;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final DataRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("User Tried to login using Spring Security");
        log.info("Authenticating user: " + username);
        return repository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

}
