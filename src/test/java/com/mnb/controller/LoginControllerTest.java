package com.mnb.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.mnb.controller.LoginController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

//    @Test
	public void testShowMyLoginPage() throws Exception {
		mockMvc.perform(get("/login")).andExpect(status().isOk()).andExpect(view().name("login"));
	}

//    @Test
	public void testShowMustGoOn() throws Exception {
		mockMvc.perform(get("/index")).andExpect(status().isOk()).andExpect(view().name("index"));
	}

	@Test
	@WithMockUser(value = "sirine", roles = { "USER" }, password = "test123")
	public void testShowAccessDenied() throws Exception {
		mockMvc.perform(post("/access-denied")).andExpect(status().isOk())
				.andExpect(view().name("access-denied"));
	}
}
