package com.mnb.controller;

import com.mnb.entity.Publisher;
import com.mnb.service.PublisherService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublisherController.class)
@WithMockUser(value = "sirine", roles = { "USER" }, password = "test123")
public class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    private List<Publisher> mockPublishers;

    @BeforeEach
    public void setUp() {
        // Mock data for tests
        mockPublishers = new ArrayList<>();
        mockPublishers.add(new Publisher() {{
            setId(1);
            setPublisherName("Publisher One");
            setDescription("Description for Publisher One");
        }});
        mockPublishers.add(new Publisher() {{
            setId(2);
            setPublisherName("Publisher Two");
            setDescription("Description for Publisher Two");
        }});
    }

    @AfterEach
    public void tearDown() {
        mockPublishers.clear();
    }

    @Test
    public void testListPublishers() throws Exception {
        when(publisherService.findAll()).thenReturn(mockPublishers);

        mockMvc.perform(get("/publisher/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("list-publishers"))
                .andExpect(model().attributeExists("publishers"))
                .andExpect(model().attribute("publishers", mockPublishers));

        verify(publisherService, times(1)).findAll();
    }

    @Test
    public void testShowFormForAdd() throws Exception {
        mockMvc.perform(get("/publisher/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("publisher-form"))
                .andExpect(model().attributeExists("publishers"));

        verifyNoInteractions(publisherService);
    }

    @Test
    public void testShowFormForUpdate() throws Exception {
        Publisher mockPublisher = mockPublishers.get(0);
        when(publisherService.findById(1)).thenReturn(mockPublisher);

        mockMvc.perform(get("/publisher/showFormForUpdate").param("publisherId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("publisher-form"))
                .andExpect(model().attributeExists("publishers"))
                .andExpect(model().attribute("publishers", mockPublisher));

        verify(publisherService, times(1)).findById(1);
    }

    @Test
    public void testSavePublisher() throws Exception {
        ArgumentCaptor<Publisher> captor = ArgumentCaptor.forClass(Publisher.class);

        mockMvc.perform(post("/publisher/save")
                        .param("id", "3")
                        .param("publisherName", "New Publisher")
                        .param("description", "Description for New Publisher"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/publisher/list"));

        verify(publisherService, times(1)).save(captor.capture());

        Publisher savedPublisher = captor.getValue();
        assertThat(savedPublisher.getId()).isEqualTo(3);
        assertThat(savedPublisher.getPublisherName()).isEqualTo("New Publisher");
        assertThat(savedPublisher.getDescription()).isEqualTo("Description for New Publisher");
    }

    @Test
    public void testDeletePublisher() throws Exception {
        mockMvc.perform(get("/publisher/delete").param("publisherId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/publisher/list"));

        verify(publisherService, times(1)).deleteById(1);
    }
}
