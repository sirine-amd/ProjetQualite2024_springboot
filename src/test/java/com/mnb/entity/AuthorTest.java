package com.mnb.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mnb.repository.AuthorRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AuthorTest {

    @Autowired
    private AuthorRepository authorRepository;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setAuthorName("Test Author");
        author.setDescription("This is a test author.");
    }

    @Test
    void testAuthorCreation() {
        Author savedAuthor = authorRepository.save(author);
        assertNotNull(savedAuthor.getId(), "Author should have a generated ID");
        assertEquals("Test Author", savedAuthor.getAuthorName(), "Author name should match");
        assertEquals("This is a test author.", savedAuthor.getDescription(), "Description should match");
    }

    @Test
    void testAuthorUpdate() {
        Author savedAuthor = authorRepository.save(author);
        savedAuthor.setAuthorName("Updated Author");
        Author updatedAuthor = authorRepository.save(savedAuthor);
        assertEquals("Updated Author", updatedAuthor.getAuthorName(), "Author name should be updated");
    }

    @Test
    void testAuthorDeletion() {
        Author savedAuthor = authorRepository.save(author);
        authorRepository.delete(savedAuthor);
        assertFalse(authorRepository.findById(savedAuthor.getId()).isPresent(), "Author should be deleted");
    }
}
