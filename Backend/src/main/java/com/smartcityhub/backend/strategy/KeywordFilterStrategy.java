package com.smartcityhub.backend.strategy;

import com.smartcityhub.backend.model.Event;
import java.util.List;
import java.util.Locale;

public class KeywordFilterStrategy implements EventFilterStrategy {

    @Override
    public List<Event> filter(List<Event> events, String value) {
        if (value == null || value.isBlank()) {
            return events;
        }

        String keyword = value.trim().toLowerCase(Locale.ROOT);

        return events.stream()
                .filter(event -> containsIgnoreCase(event.getTitle(), keyword)
                        || containsIgnoreCase(event.getDescription(), keyword))
                .toList();
    }

    private boolean containsIgnoreCase(String text, String keyword) {
        return text != null && text.toLowerCase(Locale.ROOT).contains(keyword);
    }
}
