package com.smartcityhub.backend.service;

import com.smartcityhub.backend.model.Event;
import com.smartcityhub.backend.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventServiceTest {

    private EventRepository repository;
    private EventService service;

    @BeforeEach
    void setUp() {
        repository = mock(EventRepository.class);
        service = new EventService(repository);

        when(repository.findAll()).thenReturn(List.of(
                new Event("Music Festival", "Outdoor concert", "Cultural",
                        "Downtown", LocalDateTime.of(2026, 11, 21, 18, 0)),
                new Event("Park Cleanup", "Volunteer neighborhood service", "Community Service",
                        "City Park", LocalDateTime.of(2026, 11, 22, 9, 0)),
                new Event("Safety Workshop", "Emergency preparedness", "Public Safety",
                        "City Hall", LocalDateTime.of(2026, 11, 21, 10, 0))
        ));
    }

    @Test
    void returnsAllEventsWithoutFilters() {
        assertEquals(3, service.getAllEvents(null, null, null).size());
        verify(repository).findAll();
    }

    @Test
    void filtersByCategoryIgnoringCase() {
        var result = service.getAllEvents("cultural", null, null);
        assertEquals(1, result.size());
        assertEquals("Music Festival", result.get(0).getTitle());
    }

    @Test
    void filtersByDate() {
        var result = service.getAllEvents(null, "2026-11-22", null);
        assertEquals(1, result.size());
        assertEquals("Park Cleanup", result.get(0).getTitle());
    }

    @Test
    void searchesTitleIgnoringCase() {
        var result = service.getAllEvents(null, null, "MUSIC");
        assertEquals(1, result.size());
        assertEquals("Music Festival", result.get(0).getTitle());
    }

    @Test
    void searchesDescriptionIgnoringCase() {
        var result = service.getAllEvents(null, null, "EMERGENCY");
        assertEquals(1, result.size());
        assertEquals("Safety Workshop", result.get(0).getTitle());
    }

    @Test
    void combinesKeywordCategoryAndDateFilters() {
        var result = service.getAllEvents("Cultural", "2026-11-21", "concert");
        assertEquals(1, result.size());
        assertEquals("Music Festival", result.get(0).getTitle());
    }

    @Test
    void returnsEmptyListWhenNoEventsMatch() {
        assertTrue(service.getAllEvents(null, null, "nonexistent").isEmpty());
    }

    @Test
    void ignoresBlankKeyword() {
        assertEquals(3, service.getAllEvents(null, null, "   ").size());
    }

    @Test
    void rejectsInvalidDate() {
        assertThrows(java.time.format.DateTimeParseException.class,
                () -> service.getAllEvents(null, "not-a-date", null));
    }
}
