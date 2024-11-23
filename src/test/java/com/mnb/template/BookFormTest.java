package com.mnb.template;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.mnb.controller.BookController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(value = "sirine", roles = { "USER" }, password = "test123")
class BookFormTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private BookController bookController;

    @Test
    void testRenderBookFormPage() throws Exception {
        mockMvc.perform(get("/books/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("book-form"))
                .andExpect(model().attributeExists("books"))
                .andExpect(content().string(containsString("<h3>Book Directory</h3>")))  // Check for a unique page element
                .andExpect(content().string(containsString("Save Book")))   // Ensure form header exists
                .andExpect(content().string(containsString("Book Name:"))); // Ensure form fields are present
    }

    @Test
    void testDesignValidation() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/showFormForAdd"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Document document = Jsoup.parse(content);

        // Validate form field placeholders
        assertThat(document.select("input[placeholder=Book name]").size(), greaterThan(0));
        assertThat(document.select("input[placeholder=Book subname]").size(), greaterThan(0));
        assertThat(document.select("input[placeholder=Serial Name]").size(), greaterThan(0));
        assertThat(document.select("input[placeholder=Publisher]").size(), greaterThan(0));
        assertThat(document.select("input[placeholder=Description]").size(), greaterThan(0));
        assertThat(document.select("input[placeholder=ISBN]").size(), greaterThan(0));

        // Validate the presence of the save button
        assertThat(document.select("button.btn.btn-info").text(), is("Save"));
    }

    @Test
    void testNamingConventionAdherence() throws Exception {
        mockMvc.perform(get("/books/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Book Directory")))
                .andExpect(content().string(containsString("Save Book")))
                .andExpect(content().string(containsString("Book Name:")))
                .andExpect(content().string(containsString("Book's Author")));
    }

    @Test
    void testResponsiveDesign() throws Exception {
        mockMvc.perform(get("/books/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">")))
                .andExpect(content().string(containsString("class=\"container\"")))
                .andExpect(content().string(containsString("btn btn-info col-2")))
                .andExpect(content().string(containsString("form-control mb-4 col-4")));
    }
}
