package com.mnb.repository;

import com.mnb.entity.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    private Author testAuthor;

    @BeforeEach
    void setUp() {
        // Initialize a test Author object before each test
        testAuthor = new Author();
        testAuthor.setAuthorName("John Doe");
        testAuthor.setDescription("Famous author of many books.");
    }

    @Test
    void testSaveAuthor() {
        // Save the author and check if the author was saved
        Author savedAuthor = authorRepository.save(testAuthor);
        assertNotNull(savedAuthor.getId());  // Ensure the author has been assigned an ID after saving
        assertEquals(testAuthor.getAuthorName(), savedAuthor.getAuthorName());
    }

    @Test
    void testFindAuthorById() {
        // Save the author and then find by ID
        Author savedAuthor = authorRepository.save(testAuthor);
        Author foundAuthor = authorRepository.findById(savedAuthor.getId()).orElse(null);
        assertNotNull(foundAuthor);
        assertEquals(savedAuthor.getId(), foundAuthor.getId());
    }

    @Test
    void testFindAuthorByName() {
        // Save the author and find by name
        authorRepository.save(testAuthor);
        Author foundAuthor = authorRepository.findByAuthorName("John Doe");  // Assuming you have a custom query for name
        assertNotNull(foundAuthor);
        assertEquals(testAuthor.getAuthorName(), foundAuthor.getAuthorName());
    }

    @Test
    void testDeleteAuthor() {
        // Save the author and then delete it
        Author savedAuthor = authorRepository.save(testAuthor);
        authorRepository.delete(savedAuthor);
        assertFalse(authorRepository.existsById(savedAuthor.getId()));  // Verify the author is deleted
    }

    @Test
    void testCountAuthors() {
        // Save some authors and check the count
        authorRepository.save(testAuthor);
        long countBefore = authorRepository.count();

        Author newAuthor = new Author();
        newAuthor.setAuthorName("Jane Smith");
        newAuthor.setDescription("A well-known author.");
        authorRepository.save(newAuthor);

        long countAfter = authorRepository.count();
        assertEquals(countBefore + 1, countAfter);  // The count should increase by 1
    }
}
