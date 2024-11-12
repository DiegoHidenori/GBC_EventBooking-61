package ca.gbc.eventservice.dto;

public record EventRequest(
        String eventId,
        String eventName,
        String organizerId,
        String eventType,
        int expectedAttendees
) {}
