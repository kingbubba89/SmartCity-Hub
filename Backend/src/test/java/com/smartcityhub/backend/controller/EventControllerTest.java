
package com.smartcityhub.backend.controller;

import com.smartcityhub.backend.model.Event;
import com.smartcityhub.backend.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EventControllerTest {

    private MockMvc mockMvc;
    private EventService eventService;

    @BeforeEach
    void setUp() {
        eventService = mock(EventService.class);

        EventController controller =
                new EventController(eventService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnAllEvents() throws Exception {
        Event event = new Event(
                "Fall Food Festival",
                "Community food festival",
                "Cultural",
                "Downtown Plaza",
                LocalDateTime.of(2026, 11, 21, 11, 0)
        );

        when(eventService.getAllEvents(null, null, null, null))
                .thenReturn(List.of(event));

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title")
                        .value("Fall Food Festival"))
                .andExpect(jsonPath("$[0].category")
                        .value("Cultural"));

        verify(eventService)
                .getAllEvents(null, null, null, null);
    }

    @Test
    void shouldPassCategoryFilterToService() throws Exception {
        when(eventService.getAllEvents("Cultural", null, null, null))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/events")
                        .param("category", "Cultural"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(eventService)
                .getAllEvents("Cultural", null, null, null);
    }

    @Test
    void shouldPassDateFilterToService() throws Exception {
        when(eventService.getAllEvents(null, "2026-11-21", null, null))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/events")
                        .param("date", "2026-11-21"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(eventService)
                .getAllEvents(null, "2026-11-21", null, null);
    }

    @Test
    void shouldPassKeywordFilterToService() throws Exception {
        when(eventService.getAllEvents(null, null, "music", null))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/events")
                        .param("keyword", "music"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(eventService)
                .getAllEvents(null, null, "music", null);
    }

    @Test
    void shouldPassLocationFilterToService() throws Exception {
        when(eventService.getAllEvents(null, null, null, "Downtown"))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/events")
                        .param("location", "Downtown"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(eventService)
                .getAllEvents(null, null, null, "Downtown");
    }
}
