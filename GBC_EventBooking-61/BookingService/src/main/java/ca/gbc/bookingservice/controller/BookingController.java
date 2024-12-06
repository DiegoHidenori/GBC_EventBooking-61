package ca.gbc.bookingservice.controller;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {

        log.info("Received booking request: {}", bookingRequest);
        BookingResponse createdBooking = bookingService.createBooking(bookingRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/booking/" + createdBooking.bookingId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(createdBooking);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponse> getAllBookings() {

        return bookingService.getAllBookings();

    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<?> updateBooking(
            @PathVariable String bookingId,
            @RequestBody BookingRequest bookingRequest) {

        BookingResponse updatedBooking = bookingService.updateBooking(bookingId, bookingRequest);
        return ResponseEntity.ok(updatedBooking);
    }

    @GetMapping("/{bookingId}/exists")
    public ResponseEntity<Boolean> bookingExists(@PathVariable String bookingId) {
        boolean exists = bookingService.doesBookingExist(bookingId);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> deleteBooking(@PathVariable String bookingId) {

        bookingService.deleteBooking(bookingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkRoomAvailability(
            @RequestParam Long roomId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {

        log.info("Received request to check room availability for room ID: {}, from {} to {}",
                roomId, startTime, endTime);

        boolean isAvailable = bookingService.isRoomAvailable(roomId, startTime, endTime, null);
        return ResponseEntity.ok(isAvailable);
    }
}
