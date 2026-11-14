package com.smartcityhub.backend.service;

import com.smartcityhub.backend.model.Event;
import com.smartcityhub.backend.repository.EventRepository;
import com.smartcityhub.backend.strategy.DateFilterStrategy;
import com.smartcityhub.backend.strategy.EventFilterStrategy;
import com.smartcityhub.backend.strategy.EventTypeFilterStrategy;
import com.smartcityhub.backend.strategy.KeywordFilterStrategy;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    // Keep the original two-parameter method for existing callers.
    public List<Event> getAllEvents(String category, String date) {
        return getAllEvents(category, date, null);
    }

    public List<Event> getAllEvents(String category, String date, String keyword) {

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

        return events;
    }
}
