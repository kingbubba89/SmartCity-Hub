package com.smartcityhub.backend.strategy;

import com.smartcityhub.backend.model.Event;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class KeywordFilterStrategyTest {
    private final KeywordFilterStrategy strategy = new KeywordFilterStrategy();

    @Test
    void matchesTitleOrDescriptionAndTrimsInput() {
        var events = List.of(
                new Event("Art Fair", null, "Cultural", "Downtown", LocalDateTime.now()),
                new Event("City Meetup", "Local ART workshops", "Cultural", "Library", LocalDateTime.now()),
                new Event("Cleanup", "Help the park", "Community Service", "Park", LocalDateTime.now())
        );

        var result = strategy.filter(events, "  art  ");
        assertEquals(2, result.size());
    }

    @Test
    void noMatchReturnsEmptyList() {
        var events = List.of(new Event("Concert", "Live music", "Cultural", "Park", LocalDateTime.now()));
        assertTrue(strategy.filter(events, "dance").isEmpty());
    }

    @Test
    void blankOrNullKeywordDoesNotFilter() {
        var events = List.of(new Event("Concert", "Live music", "Cultural", "Park", LocalDateTime.now()));
        assertEquals(events, strategy.filter(events, " "));
        assertEquals(events, strategy.filter(events, null));
    }
}
