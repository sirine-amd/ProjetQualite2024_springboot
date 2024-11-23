package com.mnb.service;

import com.mnb.repository.AuthorRepository;
import com.mnb.entity.Author;
import com.mnb.entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    private Author author;
    private Book book;

    // Prepare test data before each test
    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1);
        author.setAuthorName("Test Author");
        book = new Book();
        book.setId(1);
        book.setBookName("Test Book");
    }

    // Test findAll method
    @Test
    void testFindAll() {
        Author author1 = new Author();
        author1.setId(2);
        author1.setAuthorName("Author 2");

        when(authorRepository.findAll()).thenReturn(List.of(author, author1));

        List<Author> authors = authorService.findAll();
        assertNotNull(authors);
        assertEquals(2, authors.size());
    }

    // Test findById method when author is found
    @Test
    void testFindByIdFound() {
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        Author foundAuthor = authorService.findById(1);
        assertNotNull(foundAuthor);
        assertEquals("Test Author", foundAuthor.getAuthorName());
    }

    // Test findById method when author is not found
    @Test
    void testFindByIdNotFound() {
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authorService.findById(1);
        });

        assertEquals("Did not find author id - 1", exception.getMessage());
    }

    // Test save method
    @Test
    void testSave() {
        when(authorRepository.save(author)).thenReturn(author);

        Author savedAuthor = authorService.save(author);
        assertNotNull(savedAuthor);
        assertEquals("Test Author", savedAuthor.getAuthorName());
    }

    // Test deleteById method
    @Test
    void testDeleteById() {
        doNothing().when(authorRepository).deleteById(1);

        authorService.deleteById(1);

        verify(authorRepository, times(1)).deleteById(1);
    }

    // Test addBook method - book is added to the author's book list
    @Test
    void testAddBook() {
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        authorService.addBook(author, book);

        assertTrue(author.getBooksList().contains(book));
    }

    // Test addBook method - author not found
    @Test
    void testAddBookAuthorNotFound() {
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        authorService.addBook(author, book);

        // Since the author is not found, the book should not be added
        assertTrue(author.getBooksList().isEmpty());
    }

    // Test getAuthor method
    @Test
    void testGetAuthorFound() {
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        Optional<Author> foundAuthor = authorService.getAuthor(1);

        assertTrue(foundAuthor.isPresent());
        assertEquals("Test Author", foundAuthor.get().getAuthorName());
    }

    // Test getAuthor method when author is not found
    @Test
    void testGetAuthorNotFound() {
        when(authorRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Author> foundAuthor = authorService.getAuthor(1);

        assertFalse(foundAuthor.isPresent());
    }

    // Test adding a null book to the author's list
    @Test
    void testAddNullBook() {
        when(authorRepository.findById(1)).thenReturn(Optional.of(author));

        // Try adding a null book
        authorService.addBook(author, null);

        // The author's book list should not be modified
        assertTrue(author.getBooksList().isEmpty());
    }
}
