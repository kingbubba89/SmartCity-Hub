package com.smartcityhub.backend.strategy;

import com.smartcityhub.backend.model.Event;
import java.util.List;

public class EventTypeFilterStrategy implements EventFilterStrategy {

    @Override
    public List<Event> filter(List<Event> events, String value) {

        return events.stream()
                .filter(event ->
                        event.getCategory() != null &&
                                event.getCategory().equalsIgnoreCase(value))
                .toList();
    }
}