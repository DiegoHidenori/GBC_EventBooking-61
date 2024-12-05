package ca.gbc.eventservice.dto;

public record EventRequest(
        String eventName,
        Long organizerId,
        String eventType,
        int expectedAttendees,
        String bookingId
) {}
