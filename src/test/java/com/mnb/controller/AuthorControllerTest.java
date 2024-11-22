package com.mnb.controller;

import com.mnb.entity.Author;
import com.mnb.service.AuthorService;
import com.mnb.service.BookService;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(value = "sirine", roles = { "USER" }, password = "test123")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthorService authorService;

    @Mock
    private BookService bookService;

    @InjectMocks
    private AuthorController authorController;

   private Author author;

    @BeforeEach
    public void setUp() {
    	Author author = new Author();
        author.setAuthorName("John");
        author.setId(1);
        author.setDescription("Doe");
        author= authorService.save(author);
        System.out.println(author);
    }

    @Test
    public void testListAuthors() throws Exception {
        // Prepare mock data
        List<Author> authors = Arrays.asList(author);
        when(authorService.findAll()).thenReturn(authors);

        // Perform the GET request to /author/list
        mockMvc.perform(get("/author/list"))
                .andExpect(status().isOk()) // Assert that the status is OK
                .andExpect(view().name("list-authors")) // Assert that the correct view is returned
                .andExpect(model().attribute("authors", authors)); // Assert that the model contains the authors
    }

    @Test
    public void testShowFormForAdd() throws Exception {
        // Perform the GET request to /author/showFormForAdd
        mockMvc.perform(get("/author/showFormForAdd"))
                .andExpect(status().isOk()) // Assert that the status is OK
                .andExpect(view().name("author-form")) // Assert that the correct view is returned
                .andExpect(model().attributeExists("authors")); // Assert that the model contains the 'authors' attribute
    }

    @Test
    public void testShowFormForUpdate() throws Exception {
        // Prepare mock data
        when(authorService.findById(1)).thenReturn(author);

        // Perform the GET request to /author/showFormForUpdate with a parameter
        mockMvc.perform(get("/author/showFormForUpdate")
                        .param("authorId",author.getId().toString()))
                .andExpect(status().isOk()) // Assert that the status is OK
                .andExpect(view().name("author-form")) // Assert that the correct view is returned
                .andExpect(model().attribute("authors", author)); // Assert that the model contains the 'authors' attribute
    }

    @Test
    public void testSaveAuthor() throws Exception {
        // Perform the POST request to /author/save with valid author data
        mockMvc.perform(post("/author/save")
                        .param("firstName", "John")
                        .param("lastName", "Doe"))
                .andExpect(status().is3xxRedirection()) // Assert that the status is a redirect
                .andExpect(redirectedUrl("/author/list")); // Assert that the redirect URL is correct

        // Verify that the save method was called on the AuthorService
        verify(authorService, times(1)).save(any(Author.class));
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        // Perform the GET request to /author/delete with an author ID parameter
        mockMvc.perform(get("/author/delete")
                        .param("authorId", "1"))
                .andExpect(status().is3xxRedirection()) // Assert that the status is a redirect
                .andExpect(redirectedUrl("/author/list")); // Assert that the redirect URL is correct

        // Verify that the delete method was called on the AuthorService
        verify(authorService, times(1)).deleteById(1);
    }
}
