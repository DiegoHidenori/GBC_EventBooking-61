package ca.gbc.bookingservice.dto;

// To make sure that the content is immutable for sending and receiving requests
public record BookingRequest(
        String bookingId,
        String userId,
        String roomId,
        String startTime,
        String endTime,
        String purpose
) {}