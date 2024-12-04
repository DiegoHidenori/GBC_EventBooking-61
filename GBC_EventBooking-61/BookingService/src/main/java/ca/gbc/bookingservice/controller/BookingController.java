package ca.gbc.bookingservice.controller;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// Extension of @Controller but for JSON?
@Slf4j
@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest bookingRequest) {

        try {
            BookingResponse createdBooking = bookingService.createBooking(bookingRequest);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", "/api/booking/" + createdBooking.bookingId());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(createdBooking);

        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "An unexpected error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingResponse> getAllBookings() {

        return bookingService.getAllBookings();

    }

    @PutMapping("/{bookingId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> updateBooking(@PathVariable("bookingId") String bookingId,
                                           @RequestBody BookingRequest bookingRequest) {

        String updatedBookingId = bookingService.updateBooking(bookingId, bookingRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/booking" + updatedBookingId);

        return new ResponseEntity<>(headers, HttpStatus.NO_CONTENT);

    }

    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("bookingId") String bookingId) {

        bookingService.deleteBooking(bookingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @GetMapping("/check-availability")
    public ResponseEntity<Boolean> checkBookingAvailability(
            @RequestParam Long roomId,
            @RequestParam LocalDateTime startTime,
            @RequestParam LocalDateTime endTime) {

        boolean isAvailable = bookingService.checkRoomAvailability(roomId, startTime, endTime);
        return ResponseEntity.ok(isAvailable);
    }


}
