package com.mnb.template;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
class ListBooksTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private BookController bookController;
    
    @Test
    void testRenderBooksListPage() throws Exception {
        mockMvc.perform(get("/books/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-books"))
                .andExpect(model().attributeExists("books"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<h3>BOOKS LIST</h3>")))  // Check for a unique page element
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Add Book")))   // Ensure "Add Book" button exists
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Book Name")))   // Ensure table header exists
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Books Author")));  // Ensure table header exists
    }

    @Test
    void testDesignValidation() throws Exception {
        MvcResult result = mockMvc.perform(get("/books/list"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Document document = Jsoup.parse(content);

        // Validate that the "Add Book" button exists
        assertThat(document.select("a.btn.btn-primary").text(), is("Add Book"));

        // Validate the presence of the table
        assertThat(document.select("table.table-bordered").size(), greaterThan(0));

        // Check for the table header
        assertThat(document.select("thead.thead-dark").size(), greaterThan(0));
    }

    @Test
    void testNamingConventionAdherence() throws Exception {
        mockMvc.perform(get("/books/list"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Book Name")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Books Author")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Add Book")));
    }

    @Test
    void testResponsiveDesign() throws Exception {
        mockMvc.perform(get("/books/list"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("class=\"container\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("btn btn-primary btn-sm mb-3")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("class=\"table table-bordered table-striped\"")));
    }
}
