package com.smartcityhub.backend.strategy;

import com.smartcityhub.backend.model.Event;
import java.time.LocalDate;
import java.util.List;

public class DateFilterStrategy implements EventFilterStrategy {

    @Override
    public List<Event> filter(List<Event> events, String value) {

        LocalDate date = LocalDate.parse(value);

        return events.stream()
                .filter(event ->
                        event.getEventDate() != null &&
                                event.getEventDate().toLocalDate().equals(date))
                .toList();
    }
}