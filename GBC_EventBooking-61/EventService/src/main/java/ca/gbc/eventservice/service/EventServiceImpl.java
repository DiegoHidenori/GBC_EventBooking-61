package ca.gbc.eventservice.service;

import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.dto.EventResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class EventServiceImpl implements EventService {
    @Override
    public EventResponse createEvent(EventRequest eventRequest) {
        return null;
    }

    @Override
    public List<EventResponse> getAllEvents() {
        return List.of();
    }

    @Override
    public String updateEvent(String eventId, EventRequest eventRequest) {
        return "";
    }

    @Override
    public void deleteEvent(String eventId) {

    }
}
