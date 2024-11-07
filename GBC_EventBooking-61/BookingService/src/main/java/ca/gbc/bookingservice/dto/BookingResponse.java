package ca.gbc.bookingservice.dto;

public record BookingResponse(
        String bookingId,
        String userId,
        String roomId,
        String startTime,
        String endTime,
        String purpose
) {}
