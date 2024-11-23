package com.mnb.service;

import com.mnb.repository.BookRepository;
import com.mnb.entity.Book;
import com.mnb.exception.NotFoundException;
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
public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;

    // Prepare test data before each test
    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1);
        book.setBookName("Test Book");
        book.setDescription("Test Author");
    }

    // Test findAll method
    @Test
    void testFindAll() {
        Book book1 = new Book();
        book1.setId(2);
        book1.setBookName("Another Book");
        book1.setDescription("Another Author");

        when(bookRepository.findAll()).thenReturn(List.of(book, book1));

        List<Book> books = bookService.findAll();
        assertNotNull(books);
        assertEquals(2, books.size());
        assertEquals("Test Book", books.get(0).getBookName());
    }

    // Test findById method when book is found
    @Test
    void testFindByIdFound() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        Book foundBook = bookService.findById(1);
        assertNotNull(foundBook);
        assertEquals("Test Book", foundBook.getBookName());
    }

    // Test findById method when book is not found
    @Test
    void testFindByIdNotFound() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            bookService.findById(1);
        });

        assertEquals(" not found  with ID 1", exception.getMessage());
    }

    // Test save method
    @Test
    void testSave() {
        when(bookRepository.save(book)).thenReturn(book);

        bookService.save(book);

        verify(bookRepository, times(1)).save(book);
    }

    // Test deleteById method
    @Test
    void testDeleteById() {
        doNothing().when(bookRepository).deleteById(1);

        bookService.deleteById(1);

        verify(bookRepository, times(1)).deleteById(1);
    }

    // Test findBookByName method when books are found
    @Test
    void testFindBookByNameFound() {
        Book book1 = new Book();
        book1.setId(2);
        book1.setBookName("Test Book 2");
        book1.setDescription("Another Author");

        when(bookRepository.findByName("Test")).thenReturn(List.of(book, book1));

        List<Book> books = bookService.findBookByName("Test");
        assertNotNull(books);
        assertEquals(2, books.size());
        assertTrue(books.stream().anyMatch(b -> b.getBookName().contains("Test")));
    }

    // Test findBookByName method when no books are found
    @Test
    void testFindBookByNameNotFound() {
        when(bookRepository.findByName("Unknown")).thenReturn(List.of());

        List<Book> books = bookService.findBookByName("Unknown");
        assertNotNull(books);
        assertTrue(books.isEmpty());
    }

    // Test save method with a null book
    @Test
    void testSaveNullBook() {
        assertThrows(IllegalArgumentException.class, () -> {
            bookService.save(null);
        });
    }

    // Test deleteById method when book does not exist
    @Test
    void testDeleteByIdNotFound() {
        doThrow(new RuntimeException("Book not found")).when(bookRepository).deleteById(999);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookService.deleteById(999);
        });

        assertEquals("Book not found", exception.getMessage());
    }

    // Test save method with an empty book name
    @Test
    void testSaveBookWithEmptyName() {
        Book invalidBook = new Book();
        invalidBook.setId(2);
        invalidBook.setBookName("");
        invalidBook.setBooksAuthor("Test Author");

        when(bookRepository.save(invalidBook)).thenReturn(invalidBook);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookService.save(invalidBook);
        });

        assertEquals("Book name cannot be empty", exception.getMessage());
    }
}
