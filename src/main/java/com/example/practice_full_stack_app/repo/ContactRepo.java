package com.example.practice_full_stack_app.repo;

import com.example.practice_full_stack_app.domain.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
/* The @repository annotation is built on the top of the classes that manage the code related to the database.
The annotation helps to easily retrieve, update, and delete content in the database. */

public interface ContactRepo extends JpaRepository<Contact, String> {
    Optional<Contact> findById(String id);
    /* It provides a clear and explicit way to convey the message that there may not be a value, without using null.
    When getting an Optional return type, we’re likely to check if the value is missing, leading to fewer
    NullPointerExceptions in the applications.*/
}
