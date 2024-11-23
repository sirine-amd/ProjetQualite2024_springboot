package com.mnb.template;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(value = "sirine", roles = { "USER" }, password = "test123")
public class IndexPageTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testIndexPageRendering() throws Exception {
        mockMvc.perform(get("/index"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("WELCOME TO LIBRARY MANAGEMENT SYSTEM")))
                .andExpect(content().string(containsString("href=\"/books/list\"")))
                .andExpect(content().string(containsString("href=\"/author/list\"")))
                .andExpect(content().string(containsString("href=\"/publisher/list\"")))
                .andExpect(content().string(containsString("src=\"/images/books.jpg\"")))
                .andExpect(content().string(containsString("src=\"/images/authors.jpg\"")))
                .andExpect(content().string(containsString("src=\"/images/publishers.jpeg\"")));
        
        // Verify that images are accessible
        verifyImageIsAccessible("/images/books.jpg");
        verifyImageIsAccessible("/images/authors.jpg");
        verifyImageIsAccessible("/images/publishers.jpeg");
    }

    private void verifyImageIsAccessible(String imagePath) throws Exception {
        mockMvc.perform(get(imagePath))
                .andExpect(status().isOk());
    }
}

