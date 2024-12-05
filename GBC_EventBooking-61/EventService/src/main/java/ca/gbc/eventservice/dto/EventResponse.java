package ca.gbc.eventservice.dto;

public record EventResponse(
        String eventId,
        String eventName,
        Long organizerId,
        String eventType,
        int expectedAttendees,
        String bookingId
) {}
