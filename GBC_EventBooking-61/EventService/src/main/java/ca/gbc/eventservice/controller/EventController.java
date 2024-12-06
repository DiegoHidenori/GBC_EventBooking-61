package ca.gbc.eventservice.controller;

import ca.gbc.eventservice.dto.EventRequest;
import ca.gbc.eventservice.dto.EventResponse;
import ca.gbc.eventservice.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
@Slf4j
public class EventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EventResponse> createEvent(@RequestBody EventRequest eventRequest) {

        EventResponse createdEvent = eventService.createEvent(eventRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/event/" + createdEvent.eventId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdEvent);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EventResponse> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{eventId}/exists")
    public ResponseEntity<Boolean> eventExists(@PathVariable String eventId) {
        boolean exists = eventService.doesEventExist(eventId);
        return ResponseEntity.ok(exists);
    }


    @PutMapping("/{eventId}")
    public ResponseEntity<?> updateEvent(@PathVariable("eventId") String eventId,
                                        @RequestBody EventRequest eventRequest) {

        EventResponse updatedEvent = eventService.updateEvent(eventId, eventRequest);

        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{eventId}")
    public ResponseEntity<?> deleteEvent(@PathVariable("eventId") String eventId) {

        eventService.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
