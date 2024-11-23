package com.mnb.repository;

import com.mnb.entity.Book;
import com.mnb.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    // Prepare test data
    @BeforeEach
    void setUp() {
        Book book1 = new Book();
        book1.setBookName("Java Programming");
        book1.setIsbn("12345");
        book1.setSerialName("J101");
        book1.setBooksAuthor("John Doe");
        bookRepository.save(book1);

        Book book2 = new Book();
        book2.setBookName("Spring Framework");
        book2.setIsbn("67890");
        book2.setSerialName("S101");
        book2.setBooksAuthor("Jane Smith");
        bookRepository.save(book2);
    }

    @Test
    void testFindByNameWithKeywordInBookName() {
        String keyword = "Java";
        List<Book> result = bookRepository.findByName(keyword);

        // Verify that one book with the name "Java Programming" is returned
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(book -> book.getBookName().contains(keyword)));
    }

    @Test
    void testFindByNameWithKeywordInIsbn() {
        String keyword = "12345";
        List<Book> result = bookRepository.findByName(keyword);

        // Verify that the book with the ISBN "12345" is returned
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(book -> book.getIsbn().contains(keyword)));
    }

    @Test
    void testFindByNameWithNoMatch() {
        String keyword = "NonExistentBook";
        List<Book> result = bookRepository.findByName(keyword);

        // Verify that no books are found
        assertEquals(0, result.size());
    }

    @Test
    void testFindByNameWithEmptyKeyword() {
        String keyword = "";
        List<Book> result = bookRepository.findByName(keyword);

        // Verify that an empty keyword returns all books
        assertTrue(result.size() > 0);  // It should return all books stored in the database
    }
}
