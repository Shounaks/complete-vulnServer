package com.example.servingwebcontent.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.password.Md4PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailService userDetailsService;
    private final CustomAuthenticationSuccessHandler successHandler;
    private final CustomFailureHandler failureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Md4PasswordEncoder();
        //BOTTOM ONE IS SECURE!
//        return new BCryptPasswordEncoder();

    }

    @Bean
    @Primary
    protected AuthenticationManagerBuilder authManagerConfig(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return auth;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable) // We'll demonstrate CSRF and enable it later
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                                authorizationManagerRequestMatcherRegistry
                                        //Sometimes I feel I am loosing my sanity over un-debuggable things 😃
                                        .requestMatchers("/admin/**", "/admin/home").hasAuthority("ADMIN")
//                                .requestMatchers("/manager/**").hasRole("MANAGER")
//                                .requestMatchers("/user/**").hasRole("USER")
                                        .requestMatchers("/", "/auth", "/login", "/logout").permitAll()
                                        .requestMatchers("/uploads/**", "/favicon.ico").permitAll()//TO SHOW THE IMAGE TO UI
                                        .anyRequest().authenticated()
                )
                .formLogin(httpSecurityFormLoginConfigurer ->
                                httpSecurityFormLoginConfigurer
                                        .loginPage("/login").permitAll()
                                        .loginProcessingUrl("/auth").permitAll()
                                        .successHandler(successHandler)
                                        .failureHandler(failureHandler)
//                                .defaultSuccessUrl("/home",false)
                )
                .logout(LogoutConfigurer::permitAll)
                .build();
    }
}
