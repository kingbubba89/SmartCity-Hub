package com.smartcityhub.backend.service;

import com.smartcityhub.backend.model.Event;
import com.smartcityhub.backend.repository.EventRepository;
import com.smartcityhub.backend.strategy.DateFilterStrategy;
import com.smartcityhub.backend.strategy.EventFilterStrategy;
import com.smartcityhub.backend.strategy.EventTypeFilterStrategy;
import com.smartcityhub.backend.strategy.KeywordFilterStrategy;
import org.springframework.stereotype.Service;
import com.smartcityhub.backend.strategy.LocationFilterStrategy;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents(
            String category, String date, String keyword) {
        return getAllEvents(category, date, keyword, null);
    }

    public List<Event> getAllEvents(
            String category, String date, String keyword, String location) {

        List<Event> events = eventRepository.findAll();

        if (category != null && !category.isBlank()) {
            EventFilterStrategy strategy = new EventTypeFilterStrategy();
            events = strategy.filter(events, category);
        }

        if (date != null && !date.isBlank()) {
            EventFilterStrategy strategy = new DateFilterStrategy();
            events = strategy.filter(events, date);
        }

        if (keyword != null && !keyword.isBlank()) {
            EventFilterStrategy strategy = new KeywordFilterStrategy();
            events = strategy.filter(events, keyword);
        }

        if (location != null && !location.isBlank()) {
            EventFilterStrategy strategy = new LocationFilterStrategy();
            events = strategy.filter(events, location);
        }

        return events.stream()
                .filter(event -> event.getEventDate() != null)
                .sorted(java.util.Comparator.comparing(Event::getEventDate))
                .toList();
    }

}
