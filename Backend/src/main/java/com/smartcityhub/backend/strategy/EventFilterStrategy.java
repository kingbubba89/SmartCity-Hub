package com.smartcityhub.backend.strategy;

import com.smartcityhub.backend.model.Event;
import java.util.List;

public interface EventFilterStrategy {

    List<Event> filter(List<Event> events, String value);

}