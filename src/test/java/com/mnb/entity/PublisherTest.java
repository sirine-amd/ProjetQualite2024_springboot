package com.mnb.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mnb.repository.PublisherRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PublisherTest {

    @Autowired
    private PublisherRepository publisherRepository;

    private Publisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new Publisher();
        publisher.setPublisherName("Test Publisher");
        publisher.setDescription("This is a test publisher.");
    }

    @Test
    void testPublisherCreation() {
        Publisher savedPublisher = publisherRepository.save(publisher);
        assertNotNull(savedPublisher.getId(), "Publisher should have a generated ID");
        assertEquals("Test Publisher", savedPublisher.getPublisherName(), "Publisher name should match");
        assertEquals("This is a test publisher.", savedPublisher.getDescription(), "Description should match");
    }

    @Test
    void testPublisherUpdate() {
        Publisher savedPublisher = publisherRepository.save(publisher);
        savedPublisher.setPublisherName("Updated Publisher");
        Publisher updatedPublisher = publisherRepository.save(savedPublisher);
        assertEquals("Updated Publisher", updatedPublisher.getPublisherName(), "Publisher name should be updated");
    }

    @Test
    void testPublisherDeletion() {
        Publisher savedPublisher = publisherRepository.save(publisher);
        publisherRepository.delete(savedPublisher);
        assertFalse(publisherRepository.findById(savedPublisher.getId()).isPresent(), "Publisher should be deleted");
    }
}
