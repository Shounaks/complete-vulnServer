package com.example.servingwebcontent;

import com.example.servingwebcontent.repository.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DataRepository extends JpaRepository<Data,Long> {
    Optional<Data> findByNameAndPassword(String name, String password);
    Optional<Data> findByName(String name);
    List<Data> findByNameContaining(String name);
    @Query("select d.password from Data d where d.name = ?1")
    Optional<String> findDataPasswordByName(String name);
}
