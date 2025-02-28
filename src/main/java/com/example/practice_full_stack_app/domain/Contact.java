package com.example.practice_full_stack_app.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Collate;
import org.hibernate.annotations.UuidGenerator;

@Entity // JPA
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Table(name = "contacts")

// JsonInclude.Include.NON_DEFAULT: Only maps to Json what is not the default. So, for example,
// since the default for a string is null, it is not going to map this over when the string is set to null.

public class Contact {
    @Id
    @UuidGenerator
    @Column(name = "id", unique = true, updatable = false) // Serves as primary key

    private String id;
    private String name;
    private String email;
    private String title;
    private String phone;
    private String address;
    private String status;
    private String photoUrl;

}
