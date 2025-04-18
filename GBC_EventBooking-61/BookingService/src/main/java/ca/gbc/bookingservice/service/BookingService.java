package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingResponse createBooking(BookingRequest bookingRequest);
    List<BookingResponse> getAllBookings();
    BookingResponse updateBooking(String bookingId, BookingRequest bookingRequest);
    void deleteBooking(String bookingId);
//    boolean checkRoomAvailability(Long roomId, LocalDateTime startDate, LocalDateTime endDate);
    boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime, String currentBookingId);
    boolean doesBookingExist(String bookingId);
}
