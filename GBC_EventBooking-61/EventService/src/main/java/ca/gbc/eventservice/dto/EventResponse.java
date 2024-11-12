package ca.gbc.eventservice.dto;

public record EventResponse(
        String eventId,
        String eventName,
        String organizerId,
        String eventType,
        int expectedAttendees
) {}
