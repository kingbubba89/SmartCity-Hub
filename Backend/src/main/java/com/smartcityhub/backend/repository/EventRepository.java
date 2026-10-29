package com.smartcityhub.backend.repository;

import com.smartcityhub.backend.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
