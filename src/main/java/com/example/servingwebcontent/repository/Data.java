package com.example.servingwebcontent.repository;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Data implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long id;
    public String name;
    public String email;
    public String password;
    public String role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return getName();
    }
}
