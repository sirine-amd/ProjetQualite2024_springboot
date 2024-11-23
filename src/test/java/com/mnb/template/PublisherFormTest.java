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

import com.mnb.controller.PublisherController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(value = "sirine", roles = { "USER" }, password = "test123")
class PublisherFormTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private PublisherController publisherController;

    @Test
    void testRenderPublisherFormPage() throws Exception {
        mockMvc.perform(get("/publisher/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("publisher-form"))
                .andExpect(model().attributeExists("publishers"))
                .andExpect(content().string(containsString("<h3>Publisher Directory</h3>"))) 
                .andExpect(content().string(containsString("Save Publisher")));
    }

    @Test
    void testDesignValidation() throws Exception {
        MvcResult result = mockMvc.perform(get("/publisher/showFormForAdd"))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Document document = Jsoup.parse(content);

        // Validate form field placeholders
        assertThat(document.select("input[placeholder=Publisher name]").size(), greaterThan(0));
        assertThat(document.select("input[placeholder=Description]").size(), greaterThan(0));

        // Validate the presence of the save button
        assertThat(document.select("button.btn.btn-info").text(), is("Save"));
    }

    @Test
    void testNamingConventionAdherence() throws Exception {
        mockMvc.perform(get("/publisher/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Publisher Directory")))
                .andExpect(content().string(containsString("Save Publisher")));
    }

    @Test
    void testResponsiveDesign() throws Exception {
        mockMvc.perform(get("/publisher/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">")))
                .andExpect(content().string(containsString("class=\"container\"")))
                .andExpect(content().string(containsString("btn btn-info col-2")))
                .andExpect(content().string(containsString("form-control mb-4 col-4")));
    }
}
