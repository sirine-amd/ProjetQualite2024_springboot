package com.mnb.template;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(value = "sirine", roles = { "USER" }, password = "test123")
public class AccessDeniedPageTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAccessDeniedPageDesign() throws Exception {
        mockMvc.perform(post("/access-denied")) // Change GET to POST
                .andExpect(view().name("access-denied"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<title>Library System - Access Denied</title>")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Access Denied - You are not authorized to access this resource.")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Back to Home Page")));
    }

    @Test
    public void testAccessDeniedPageLink() throws Exception {
        mockMvc.perform(post("/access-denied"))
               .andExpect(content().string(org.hamcrest.Matchers.containsString("href=\"/login\""))); // Updated expected value
    }


    @Test
    public void testAccessDeniedPageWhenAccessDenied() throws Exception {
        mockMvc.perform(post("/access-denied")) // Change GET to POST
                .andExpect(status().isOk())
                .andExpect(view().name("access-denied"));
    }
}

