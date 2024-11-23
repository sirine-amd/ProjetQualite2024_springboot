package com.mnb.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import com.mnb.entity.Publisher;

import java.util.Optional;

@DataJpaTest
public class PublisherRepositoryTest {

    @Autowired
    private PublisherRepository publisherRepository;

    private Publisher publisher;

    // Prepare test data before each test
    @BeforeEach
    void setUp() {
        publisher = new Publisher();
        publisher.setId(1);
        publisher.setPublisherName("Test Publisher");
        publisher.setDescription("This is a test publisher.");
        publisherRepository.save(publisher);
    }

    // Clean up after each test to ensure no test interferes with the others
    @AfterEach
    void tearDown() {
        publisherRepository.deleteAll();
    }

    // Test for finding a Publisher by ID
    @Test
    void testFindPublisherById() {
        Publisher foundPublisher = publisherRepository.findById(1).orElse(null);
        assertNotNull(foundPublisher);  // Verify publisher exists
        assertEquals("Test Publisher", foundPublisher.getPublisherName());  // Check if the name matches
    }

    // Test for finding Publisher by name (assuming such a method exists in the repository)
    @Test
    void testFindPublisherByName() {
        Optional<Publisher> foundPublisher = publisherRepository.findByPublisherName("Test Publisher");
        assertTrue(foundPublisher.isPresent());  // Verify publisher is found
        assertEquals("Test Publisher", foundPublisher.get().getPublisherName());  // Verify the name
    }

    // Test for saving a new Publisher
    @Test
    void testSavePublisher() {
        Publisher newPublisher = new Publisher();
        newPublisher.setPublisherName("New Publisher");
        newPublisher.setDescription("This is a new publisher.");
        Publisher savedPublisher = publisherRepository.save(newPublisher);
        assertNotNull(savedPublisher);  // Ensure the saved publisher is not null
        assertEquals("New Publisher", savedPublisher.getPublisherName());  // Verify the name is saved correctly
    }

    // Test for updating a Publisher's description
    @Test
    void testUpdatePublisher() {
        publisher.setDescription("Updated description");
        Publisher updatedPublisher = publisherRepository.save(publisher);  // Save the updated publisher
        assertEquals("Updated description", updatedPublisher.getDescription());  // Verify the description is updated
    }

    // Test for deleting a Publisher
    @Test
    void testDeletePublisher() {
        publisherRepository.delete(publisher);  // Delete the publisher
        Optional<Publisher> deletedPublisher = publisherRepository.findById(1);
        assertFalse(deletedPublisher.isPresent());  // Ensure the publisher is deleted
    }

    // Test for handling a Publisher that doesn't exist
    @Test
    void testPublisherNotFound() {
        Optional<Publisher> nonExistentPublisher = publisherRepository.findById(999);  // Non-existent ID
        assertFalse(nonExistentPublisher.isPresent());  // Ensure no publisher is found
    }

    // Test for saving a Publisher with empty fields (edge case)
    @Test
    void testSavePublisherWithEmptyFields() {
        Publisher emptyPublisher = new Publisher();
        emptyPublisher.setPublisherName("");  // Set empty name
        emptyPublisher.setDescription("");  // Set empty description
        Publisher savedPublisher = publisherRepository.save(emptyPublisher);
        assertNotNull(savedPublisher);  // Ensure the publisher is saved even with empty fields
        assertEquals("", savedPublisher.getPublisherName());  // Ensure empty name is saved
        assertEquals("", savedPublisher.getDescription());  // Ensure empty description is saved
    }

    // Test for checking if publisher's name is unique
    @Test
    void testPublisherNameUniqueness() {
        Publisher anotherPublisher = new Publisher();
        anotherPublisher.setPublisherName("Test Publisher");
        anotherPublisher.setDescription("This should not be allowed.");
        assertThrows(Exception.class, () -> publisherRepository.save(anotherPublisher), "Expected exception for duplicate publisher name");
    }
}
