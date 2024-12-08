package ca.gbc.eventservice.service;

import ca.gbc.bookingservice.event.BookingPlacedEvent;
import ca.gbc.eventservice.client.BookingClient;
import ca.gbc.eventservice.client.UserClient;
import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.dto.EventResponse;
import ca.gbc.eventservice.exception.BookingNotFoundException;
import ca.gbc.eventservice.exception.UserNotFoundException;
import ca.gbc.eventservice.model.Event;
import ca.gbc.eventservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
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
    private final UserClient userClient;
    private final BookingClient bookingClient;
    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "booking-placed")
    public void listen(BookingPlacedEvent bookingPlacedEvent) {

        log.info("Received message from booking-placed topic {}", bookingPlacedEvent);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("comp3095@georgebrown.ca");
            messageHelper.setTo(bookingPlacedEvent.getEmail());
            messageHelper.setSubject(String.format("Your Booking (%s) was placed successfully",
                    bookingPlacedEvent.getBookingNumber()));
            messageHelper.setText(String.format("""
                    
                    Good Day %s %s,
                    
                    Your booking with booking number %s was successfully placed
                    
                    Thank you,
                    COMP3095
                    
                    """,
                    bookingPlacedEvent.getFirstName(),
                    bookingPlacedEvent.getLastName(),
                    bookingPlacedEvent.getBookingNumber()

            ));
        };

        try {

            javaMailSender.send(messagePreparator);
            log.info("Booking notification successfully sent");

        } catch (MailException e) {
            log.error("Error occurred when sending email", e);
            throw new RuntimeException("Exception occurred when trying to send email", e);
        }

    }

    @Override
    public EventResponse createEvent(EventRequest eventRequest) {

        log.info("Processing event request: {}", eventRequest);

        String bookingId = eventRequest.bookingId();
        Long userId = eventRequest.organizerId();
        String userType = userClient.getUserType(userId);

        log.info("Checking user with ID: {} exists.", userId);
        if (!userClient.userExists(userId)) {
            log.error("User with ID {} does not exist", userId);
            throw new UserNotFoundException("User with ID " + userId + " does not exist");
        }

        log.info("Checking userType of user with ID: {}.", userId);
        if ("Student".equalsIgnoreCase(userType) && eventRequest.expectedAttendees() > 50) {
            log.error("Students cannot organize events with more than 50 attendees.");
            throw new IllegalArgumentException("Students cannot organize events with more than 50 attendees.");
        }

        if ("Other".equalsIgnoreCase(userType)) {
            log.error("Users with userType 'Other' cannot organize events.");
            throw new IllegalArgumentException("Users with userType 'Other' cannot organize events.");
        }

        log.info("Checking booking with ID: {} exists.", bookingId);
        if (!bookingClient.bookingExists(bookingId)) {
            log.error("Booking with ID {} does not exist", bookingId);
            throw new BookingNotFoundException("Booking with ID " + bookingId + " does not exist");
        }

        Event event = Event.builder()
                .eventName(eventRequest.eventName())
                .organizerId(eventRequest.organizerId())
                .eventType(eventRequest.eventType())
                .expectedAttendees(eventRequest.expectedAttendees())
                .bookingId(eventRequest.bookingId())
                .build();

        // Persist the product, used the @RequiredArgsConstructor to inject the BookingRepository
        eventRepository.save(event);

        log.info("Event created successfully with ID: {}", event.getEventName());

        return new EventResponse(
                event.getEventId(),
                event.getEventName(),
                event.getOrganizerId(),
                event.getEventType(),
                event.getExpectedAttendees(),
                event.getBookingId()
        );

    }

    @Override
    public List<EventResponse> getAllEvents() {

        log.info("Retrieving all events");
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
                event.getExpectedAttendees(),
                event.getBookingId()

        );


    }

    @Override
    public EventResponse updateEvent(String eventId, EventRequest eventRequest) {

        log.info("Attempting to update event with ID: {}", eventId);

        String bookingId = eventRequest.bookingId();
        Long userId = eventRequest.organizerId();
        String userType = userClient.getUserType(userId);

        log.info("Checking user with ID: {} exists.", userId);
        if (!userClient.userExists(userId)) {
            log.error("User with ID {} does not exist", userId);
            throw new UserNotFoundException("User with ID " + userId + " does not exist");
        }

        log.info("Checking userType of user with ID: {}.", userId);
        if ("Student".equalsIgnoreCase(userType) && eventRequest.expectedAttendees() > 50) {
            log.error("Students cannot organize events with more than 50 attendees.");
            throw new IllegalArgumentException("Students cannot organize events with more than 50 attendees.");
        }

        if ("Other".equalsIgnoreCase(userType)) {
            log.error("Users with userType 'Other' cannot organize events.");
            throw new IllegalArgumentException("Users with userType 'Other' cannot organize events.");
        }

        log.info("Checking booking with ID: {} exists.", bookingId);
        if (!bookingClient.bookingExists(bookingId)) {
            log.error("Booking with ID {} does not exist", bookingId);
            throw new BookingNotFoundException("Booking with ID " + bookingId + " does not exist");
        }

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
        event.setBookingId(eventRequest.bookingId());

        Event updatedEvent = eventRepository.save(event);
        log.info("Event with ID: {} updated successfully", updatedEvent.getEventId());

        return mapToEventResponse(updatedEvent);

    }

    @Override
    public boolean doesEventExist(String eventId) {
        log.info("Checking if event exists with ID: {}", eventId);
        return eventRepository.existsById(eventId);
    }

    @Override
    public void deleteEvent(String eventId) {

        log.info("Attempting to delete event with ID: {}", eventId);
        eventRepository.deleteById(eventId);
        log.info("Event with ID: {} deleted successfully", eventId);

    }
}
