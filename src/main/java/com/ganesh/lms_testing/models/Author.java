package com.ganesh.lms_testing.models;

import com.ganesh.lms_testing.models.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "authors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "author")
    private Set<Book> books;
}
