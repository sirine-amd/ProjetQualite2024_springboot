package com.mnb.controller;

import com.mnb.entity.Author;
import com.mnb.entity.Book;
import com.mnb.entity.Publisher;
import com.mnb.service.AuthorService;
import com.mnb.service.BookService;
import com.mnb.service.PublisherService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
@WithMockUser(value = "sirine", roles = { "USER" }, password = "test123")
public class BookControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;
	@MockBean
	private AuthorService authorService;
	@MockBean
	private PublisherService publisherService;

	private Book mockBook;
	private Author mockAuthor;
	private Publisher mockPublisher;

	@BeforeEach
	public void setUp() {
		// Create mock Author and Publisher
		mockAuthor = new Author();
		mockAuthor.setAuthorName("Jane Austen");
		authorService.save(mockAuthor);

		mockPublisher = new Publisher();
		mockPublisher.setPublisherName("Penguin Books");
		publisherService.save(mockPublisher);
		// Create mock Book
		mockBook = new Book();
		mockBook.setBookName("Pride and Prejudice");
		mockBook.setId(1);
		mockBook.setBookSubname("A Classic Novel");
		mockBook.setSerialName("Penguin Classics");
		mockBook.setBooksAuthor("Jane Austen");
		mockBook.setBooksPublisher("Penguin Books");
		mockBook.setDescription("A story about manners and marriage...");
		mockBook.setIsbn("1234567890");
		mockBook.setAuthor(mockAuthor);
		mockBook.setPublisher(mockPublisher);

		bookService.save(mockBook);

	}

	@Test
	public void testListBooks() throws Exception {
		when(bookService.findAll()).thenReturn(Arrays.asList(mockBook));

		mockMvc.perform(get("/books/list")).andExpect(status().isOk()).andExpect(view().name("list-books"))
				.andExpect(model().attributeExists("books"))
				.andExpect(model().attribute("books", Arrays.asList(mockBook)));

		verify(bookService, times(1)).findAll();
	}

	@Test
//	@WithMockUser(value = "sarra", roles = { "USER", "ADMIN"}, password = "test123")
	public void testSaveBook() throws Exception {
	    // Setup mock data
	    Book newBook = new Book();
	    newBook.setBookName("Pjride and Prejudice");
	    newBook.setBookSubname("zeA Classic Novel");
	    newBook.setSerialName("Pezdnguin Classics");
	    newBook.setBooksAuthor("Jadezene Austen");
	    newBook.setBooksPublisher("Peeznguin Books");
	    newBook.setDescription("A stoezedry about manners and marriage...");
	    newBook.setIsbn("1234567890");

	    // Mock the service call (ensure it does not perform any real action)
	    doNothing().when(bookService).save(any(Book.class));

	    // Perform the POST request
		ResultActions perform = mockMvc.perform(post("/books/save")
	                    .flashAttr("books", newBook));
		MvcResult result = perform.andReturn();
		System.out.println(result);
		perform // Flash attribute used here
	            .andExpect(status().is3xxRedirection()) // Expect redirection
	            .andExpect(redirectedUrl("/books/list")); // Verify the redirect URL

	    // Verify the service method was called
	    verify(bookService, times(1)).save(newBook);
	}


	@Test
	public void testShowFormForAdd() throws Exception {
		mockMvc.perform(get("/books/showFormForAdd")).andExpect(status().isOk()).andExpect(view().name("book-form"))
				.andExpect(model().attributeExists("books"));
	}

	@Test
	public void testShowFormForUpdate() throws Exception {
		when(bookService.findById(anyInt())).thenReturn(mockBook);

		mockMvc.perform(get("/books/showFormForUpdate").param("bookId", "1")).andExpect(status().isOk())
				.andExpect(view().name("book-form")).andExpect(model().attributeExists("books"))
				.andExpect(model().attribute("books", mockBook));

		verify(bookService, times(1)).findById(1);
	}

	@Test
	public void testDeleteBook_NotAllowed() throws Exception {
		doNothing().when(bookService).deleteById(anyInt());

		mockMvc.perform(get("/books/delete").param("bookId", "1")).andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/login"));

		verify(bookService, times(1)).deleteById(1);
	}
	@Test
	@WithMockUser(value = "sarra", roles = { "USER", "ADMIN"}, password = "test123")
	public void testDeleteBook_Allowed() throws Exception {
		doNothing().when(bookService).deleteById(anyInt());
		
		mockMvc.perform(get("/books/delete").param("bookId", "1")).andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("/books/list"));
		
		verify(bookService, times(1)).deleteById(1);
	}

	@Test
	public void testSearchBookByName() throws Exception {
		when(bookService.findBookByName(anyString())).thenReturn(Arrays.asList(mockBook));

		mockMvc.perform(get("/books/search").param("keyword", "Pride")).andExpect(status().isOk())
				.andExpect(view().name("list-books")).andExpect(model().attributeExists("books"))
				.andExpect(model().attribute("books", Arrays.asList(mockBook)));

		verify(bookService, times(1)).findBookByName("Pride");
	}
}
