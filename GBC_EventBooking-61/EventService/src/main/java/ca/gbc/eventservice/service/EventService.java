package ca.gbc.eventservice.service;

import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.dto.EventResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventResponse createEvent(EventRequest eventRequest);
    List<EventResponse> getAllEvents();
    EventResponse updateEvent(String eventId, EventRequest eventRequest);
    void deleteEvent(String eventId);
    boolean doesEventExist(String eventId);
}