package com.mnb.template;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
class AuthorFormTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AuthorController authorController;

    @Test
    void testRenderAuthorFormPage() throws Exception {
        mockMvc.perform(get("/author/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("author-form"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(content().string(containsString("<h3>Author Directory</h3>"))) 
                .andExpect(content().string(containsString("Save Author")));; 
    }

    @Test
    void testDesignValidation() throws Exception {
        MvcResult result = mockMvc.perform(get("/author/showFormForAdd"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Document document = Jsoup.parse(content);

        // Validate form field placeholders
        assertThat(document.select("input[placeholder=Author name]").size(), greaterThan(0));
        assertThat(document.select("input[placeholder=Description]").size(), greaterThan(0));

        // Validate the presence of the save button
        assertThat(document.select("button.btn.btn-info").text(), is("Save"));
    }

    @Test
    void testNamingConventionAdherence() throws Exception {
        mockMvc.perform(get("/author/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Author Directory")))
                .andExpect(content().string(containsString("Save Author")));
    }

    @Test
    void testResponsiveDesign() throws Exception {
        mockMvc.perform(get("/author/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">")))
                .andExpect(content().string(containsString("class=\"container\"")))
                .andExpect(content().string(containsString("btn btn-info col-2")))
                .andExpect(content().string(containsString("form-control mb-4 col-4")));
    }
}
