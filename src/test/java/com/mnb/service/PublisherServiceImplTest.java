package com.mnb.service;

import com.mnb.exception.NotFoundException;
import com.mnb.repository.PublisherRepository;
import com.mnb.entity.Publisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceImplTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherServiceImpl publisherService;

    private Publisher publisher;

    // Prepare test data before each test
    @BeforeEach
    void setUp() {
        publisher = new Publisher();
        publisher.setId(1);
        publisher.setPublisherName("Test Publisher");
        publisher.setDescription("This is a test publisher.");
    }

    // Test findAll method
    @Test
    void testFindAll() {
        Publisher publisher2 = new Publisher();
        publisher2.setId(2);
        publisher2.setPublisherName("Another Publisher");
        publisher2.setDescription("Another test publisher");

        when(publisherRepository.findAll()).thenReturn(List.of(publisher, publisher2));

        List<Publisher> publishers = publisherService.findAll();
        assertNotNull(publishers);
        assertEquals(2, publishers.size());
        assertEquals("Test Publisher", publishers.get(0).getPublisherName());
    }

    // Test findById method when publisher is found
    @Test
    void testFindByIdFound() {
        when(publisherRepository.findById(1)).thenReturn(Optional.of(publisher));

        Publisher foundPublisher = publisherService.findById(1);
        assertNotNull(foundPublisher);
        assertEquals("Test Publisher", foundPublisher.getPublisherName());
    }

    // Test findById method when publisher is not found
    @Test
    void testFindByIdNotFound() {
        when(publisherRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            publisherService.findById(1);
        });

        assertEquals("Publisher not found  with ID 1", exception.getMessage());
    }

    // Test save method
    @Test
    void testSave() {
        when(publisherRepository.save(publisher)).thenReturn(publisher);

        publisherService.save(publisher);

        verify(publisherRepository, times(1)).save(publisher);
    }

    // Test deleteById method
    @Test
    void testDeleteById() {
        doNothing().when(publisherRepository).deleteById(1);

        publisherService.deleteById(1);

        verify(publisherRepository, times(1)).deleteById(1);
    }

    // Test save method with a null publisher
    @Test
    void testSaveNullPublisher() {
        assertThrows(IllegalArgumentException.class, () -> {
            publisherService.save(null);
        });
    }

    // Test deleteById method when publisher does not exist
    @Test
    void testDeleteByIdNotFound() {
        doThrow(new RuntimeException("Publisher not found")).when(publisherRepository).deleteById(999);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            publisherService.deleteById(999);
        });

        assertEquals("Publisher not found", exception.getMessage());
    }

    // Test findById method with invalid ID format (e.g., negative ID)
    @Test
    void testFindByIdInvalidId() {
        int invalidId = -1;

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            publisherService.findById(invalidId);
        });

        assertEquals("Publisher not found  with ID " + invalidId, exception.getMessage());
    }

    // Test save method with invalid publisher name
    @Test
    void testSavePublisherWithInvalidName() {
        Publisher invalidPublisher = new Publisher();
        invalidPublisher.setId(2);
        invalidPublisher.setPublisherName(""); // Empty name
        invalidPublisher.setDescription("Invalid publisher");

        when(publisherRepository.save(invalidPublisher)).thenReturn(invalidPublisher);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            publisherService.save(invalidPublisher);
        });

        assertEquals("Publisher name cannot be empty", exception.getMessage());
    }
}
