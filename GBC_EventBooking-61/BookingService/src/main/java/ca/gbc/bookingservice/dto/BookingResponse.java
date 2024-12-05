package ca.gbc.bookingservice.dto;

import java.time.LocalDateTime;


public record BookingResponse(

        String bookingId,
        Long userId,
        Long roomId,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String purpose

) {}
