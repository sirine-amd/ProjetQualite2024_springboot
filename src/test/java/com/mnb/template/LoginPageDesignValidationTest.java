package com.mnb.template;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginPageDesignValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoginPageDesign() throws Exception {
        // Perform a GET request to /login
        mockMvc.perform(get("/login"))
                // Validate if the correct view is returned
                .andExpect(view().name("login"))

                // Validate if the page contains specific elements
                .andExpect(content().string(org.hamcrest.Matchers.containsString("<title>Login Page</title>")))  // Check if the title exists
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Sign In: In-Memory Authentication")))  // Check heading text
                .andExpect(content().string(org.hamcrest.Matchers.containsString("username")))  // Check if "username" input field is present
                .andExpect(content().string(org.hamcrest.Matchers.containsString("password")))  // Check if "password" input field is present
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Login")));  // Check if "Login" button is present
    }

    @Test
    public void testLoginPageFieldNameConventions() throws Exception {
        // Perform a GET request to /login
        mockMvc.perform(get("/login"))
                // Validate if the input field 'username' has the correct 'name' attribute
                .andExpect(content().string(org.hamcrest.Matchers.containsString("name=\"username\"")))
                // Validate if the input field 'password' has the correct 'name' attribute
                .andExpect(content().string(org.hamcrest.Matchers.containsString("name=\"password\"")));
    }

    @Test
    public void testLoginPageErrorMessages() throws Exception {
        // Simulate an error scenario by adding a query parameter 'error'
        mockMvc.perform(get("/login").param("error", "true"))
                // Validate if the error message is shown on the page
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Invalid username and password")));
    }

    @Test
    public void testLoginPageLogoutMessages() throws Exception {
        // Simulate a logout scenario by adding a query parameter 'logout'
        mockMvc.perform(get("/login").param("logout", "true"))
                // Validate if the logout success message is displayed
                .andExpect(content().string(org.hamcrest.Matchers.containsString("You have been logged out.")));
    }
}
