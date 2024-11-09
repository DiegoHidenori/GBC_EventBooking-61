package ca.gbc.bookingservice.dto;

import java.time.LocalDateTime;

// To make sure that the content is immutable for sending and receiving requests
public record BookingRequest(

        String bookingId,
        String userId,
        String roomId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String purpose

) {}