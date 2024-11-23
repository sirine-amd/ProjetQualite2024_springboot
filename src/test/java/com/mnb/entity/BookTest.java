package com.mnb.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.mnb.repository.AuthorRepository;
import com.mnb.repository.BookRepository;
import com.mnb.repository.PublisherRepository;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class BookTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private Book book;
    private Publisher publisher;
    private Author author;

    @BeforeEach
    void setUp() {
        publisher = new Publisher();
        publisher.setPublisherName("Test Publisher");
        publisher.setDescription("Publisher Description");
        publisher = publisherRepository.save(publisher);

        author = new Author();
        author.setAuthorName("Test Author");
        author.setDescription("Author Description");
        author = authorRepository.save(author);

        book = new Book();
        book.setBookName("Test Book");
        book.setBooksAuthor("Test Author");
        book.setBooksPublisher("Test Publisher");
        book.setIsbn("1234567890");
        book.setPublisher(publisher);
        book.setAuthor(author);
    }

    @Test
    void testBookCreation() {
        Book savedBook = bookRepository.save(book);
        assertNotNull(savedBook.getId(), "Book should have a generated ID");
        assertEquals("Test Book", savedBook.getBookName(), "Book name should match");
        assertEquals("Test Author", savedBook.getBooksAuthor(), "Book author should match");
        assertEquals("1234567890", savedBook.getIsbn(), "ISBN should match");
        assertNotNull(savedBook.getPublisher(), "Publisher should be set");
        assertNotNull(savedBook.getAuthor(), "Author should be set");
    }

    @Test
    void testBookUpdate() {
        Book savedBook = bookRepository.save(book);
        savedBook.setBookName("Updated Book");
        Book updatedBook = bookRepository.save(savedBook);
        assertEquals("Updated Book", updatedBook.getBookName(), "Book name should be updated");
    }

    @Test
    void testBookDeletion() {
        Book savedBook = bookRepository.save(book);
        bookRepository.delete(savedBook);
        assertFalse(bookRepository.findById(savedBook.getId()).isPresent(), "Book should be deleted");
    }
}
