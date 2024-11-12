package ca.gbc.eventservice.service;

import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.dto.EventResponse;
import ca.gbc.eventservice.model.Event;
import ca.gbc.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final MongoTemplate mongoTemplate;
    private final RestTemplate restTemplate;

    @Override
    public EventResponse createEvent(EventRequest eventRequest) {

        log.info("Processing event request: {}", eventRequest);
        log.debug("Starting to process event request with details: {}", eventRequest);


        Event event = Event.builder()
                .eventName(eventRequest.eventName())
                .organizerId(eventRequest.organizerId())
                .eventType(eventRequest.eventType())
                .expectedAttendees(eventRequest.expectedAttendees())
                .build();

        // Persist the product, used the @RequiredArgsConstructor to inject the BookingRepository
        eventRepository.save(event);

        log.info("Event created successfully with ID: {}", event.getEventName());

        return new EventResponse(
                event.getEventId(),
                event.getEventName(),
                event.getOrganizerId(),
                event.getEventType(),
                event.getExpectedAttendees()
        );

    }

    @Override
    public List<EventResponse> getAllEvents() {

        log.info("Retrieving all events");
        log.debug("Fetching all events from the database");

        List<Event> events = eventRepository.findAll();
        log.info("Total events retrieved: {}", events.size());

//        return bookings.stream().map(booking -> mapToBookingResponse(booking)).toList();
        return events.stream().map(this::mapToEventResponse).toList(); // same as the one above

    }

    private EventResponse mapToEventResponse(Event event) {

        log.debug("Mapping event entity to response for event ID: {}", event.getEventId());

        return new EventResponse(

                event.getEventId(),
                event.getEventName(),
                event.getOrganizerId(),
                event.getEventType(),
                event.getExpectedAttendees());

    }

    @Override
    public EventResponse updateEvent(String eventId, EventRequest eventRequest) {

        log.info("Attempting to update event with ID: {}", eventId);
        log.debug("Attempting to update event with ID: {}", eventId);

        Query query = new Query();
        query.addCriteria(Criteria.where("eventId").is(eventId));
        Event event = mongoTemplate.findOne(query, Event.class);

        if (event == null) {
            log.warn("No event found with ID: {}", eventId);
            throw new IllegalArgumentException("Event not found");
        }

        event.setEventName(eventRequest.eventName());
        event.setOrganizerId(eventRequest.organizerId());
        event.setEventType(eventRequest.eventType());
        event.setExpectedAttendees(eventRequest.expectedAttendees());

        Event updatedEvent = eventRepository.save(event);
        log.info("Event with ID: {} updated successfully", updatedEvent.getEventId());

        return mapToEventResponse(updatedEvent);

    }

    @Override
    public void deleteEvent(String eventId) {

        log.info("Attempting to delete event with ID: {}", eventId);
        log.debug("Attempting to delete event with ID: {}", eventId);

        eventRepository.deleteById(eventId);
        log.info("Event with ID: {} deleted successfully", eventId);

    }
}
