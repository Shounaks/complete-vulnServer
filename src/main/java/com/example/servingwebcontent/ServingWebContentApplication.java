package com.example.servingwebcontent;

import com.example.servingwebcontent.repository.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.stream.IntStream;

@Slf4j
@EnableWebMvc
@SpringBootApplication
@RequiredArgsConstructor
public class ServingWebContentApplication implements CommandLineRunner {
    private final DataRepository repository;
    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ServingWebContentApplication.class, args);
    }

    @Override
    public void run(String... args) {
        IntStream.rangeClosed(1, 100)
                .mapToObj(i -> repository.save(Data.builder()
                        .name("USER_" + i)
                        .email("USER_" + i + "@gmail.com")
                        .password(passwordEncoder.encode("QWERTY"))
                        .role("STUDENT")
                        .build())
                )
                //DO NOT REMOVE THIS FOR EACH! SOMETHING WIERD IS HAPPENING
                .forEach(x -> System.out.println("saving: " + x));
        log.info("Student Data Created!");
        repository.save(Data.builder()
                .name("admin")
                .email("admin@gmail.com")
                .password(passwordEncoder.encode("admin"))
                .role("ADMIN")
                .build());
        log.info("Admin Data Created!");
    }
}
