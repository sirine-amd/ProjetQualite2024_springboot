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

import com.mnb.controller.AuthorController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(value = "sirine", roles = { "USER" }, password = "test123")
class listAuthorsTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AuthorController authorController;
    
    @Test
    void testRenderAuthorsListPage() throws Exception {
        mockMvc.perform(get("/author/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-authors"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<h3>AUTHORS LIST</h3>")))  // Check for a unique page element
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Add Author")))   // Ensure button exists
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Author Name")))   // Ensure column header exists
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Description")));  // Ensure column header exists
    }

    @Test
    void testDesignValidation() throws Exception {
        MvcResult result = mockMvc.perform(get("/author/list"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Document document = Jsoup.parse(content);

        // Validate that the "Add Author" button exists
        assertThat(document.select("a.btn.btn-primary").text(), is("Add Author"));

        // Validate the presence of the table
        assertThat(document.select("table.table-bordered").size(), greaterThan(0));

        // Check for the table header
        assertThat(document.select("thead.thead-dark").size(), greaterThan(0));
    }

    @Test
    void testNamingConventionAdherence() throws Exception {
        mockMvc.perform(get("/author/list"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Author Name")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Description")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Add Author")));
    }

    @Test
    void testResponsiveDesign() throws Exception {
        mockMvc.perform(get("/author/list"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("class=\"container\"")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("btn btn-primary btn-sm mb-3")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("class=\"table table-bordered table-striped\"")));
    }
}
