package ca.gbc.bookingservice.dto;

import java.time.LocalDateTime;

public record BookingResponse(

        String bookingId,
        String userId,
        String roomId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String purpose

) {}
