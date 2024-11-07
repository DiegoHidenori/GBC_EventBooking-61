package ca.gbc.eventservice.dto;

public record EventResponse(
        String eventName,
        String organizerId,
        String eventType,
        String expectedAttendees
) {}
