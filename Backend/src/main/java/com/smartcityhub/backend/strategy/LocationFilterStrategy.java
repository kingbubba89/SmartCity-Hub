
package com.smartcityhub.backend.strategy;

import com.smartcityhub.backend.model.Event;
import java.util.List;
import java.util.Locale;

public class LocationFilterStrategy implements EventFilterStrategy {

    @Override
    public List<Event> filter(List<Event> events, String location) {
        if (location == null || location.isBlank()) {
            return events;
        }

        String search = location.trim().toLowerCase(Locale.ROOT);

        return events.stream()
                .filter(event -> event.getLocation() != null
                        && event.getLocation()
                        .toLowerCase(Locale.ROOT)
                        .contains(search))
                .toList();
    }
}
