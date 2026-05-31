package com.ganesh.lms_testing.repository;

import com.ganesh.lms_testing.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Author a WHERE a.email = :email")
    boolean existsByEmail(String email);

    Optional<Author> findByEmail(String email);

    Optional<Author> findByNameIgnoreCase(String name);
}
