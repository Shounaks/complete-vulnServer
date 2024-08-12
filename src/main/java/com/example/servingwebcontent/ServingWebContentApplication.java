package com.example.servingwebcontent;

import com.example.servingwebcontent.repository.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.stream.IntStream;

@EnableWebMvc
@SpringBootApplication
@RequiredArgsConstructor
public class ServingWebContentApplication implements CommandLineRunner {
    private final DataRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(ServingWebContentApplication.class, args);
    }

    @Override
    public void run(String... args) {
        IntStream.rangeClosed(1, 100)
                .mapToObj(i -> repository.save(Data.builder()
                        .name("USER_" + i)
                        .email("USER_" + i + "@gmail.com")
                        .password("QWERTY")
                        .role("STUDENT")
                        .build())
                ).forEach(x -> System.out.println("saving: " + x));
    }
}
