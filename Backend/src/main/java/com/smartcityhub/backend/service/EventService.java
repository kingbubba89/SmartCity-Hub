package com.smartcityhub.backend.service;

import com.smartcityhub.backend.model.Event;
import com.smartcityhub.backend.repository.EventRepository;
import com.smartcityhub.backend.strategy.DateFilterStrategy;
import com.smartcityhub.backend.strategy.EventFilterStrategy;
import com.smartcityhub.backend.strategy.EventTypeFilterStrategy;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> getAllEvents(String category, String date) {

        List<Event> events = eventRepository.findAll();

        if (category != null && !category.isBlank()) {
            EventFilterStrategy strategy = new EventTypeFilterStrategy();
            events = strategy.filter(events, category);
        }

        if (date != null && !date.isBlank()) {
            EventFilterStrategy strategy = new DateFilterStrategy();
            events = strategy.filter(events, date);
        }

        return events;
    }
}