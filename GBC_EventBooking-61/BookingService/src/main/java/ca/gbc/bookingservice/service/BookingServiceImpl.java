package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        //***************** Add proper log throughout the file...
        log.debug("Booking request: {}", bookingRequest.bookingId());

        Booking booking = Booking.builder()
                .bookingId(bookingRequest.bookingId())
                .userId(bookingRequest.userId())
                .roomId(bookingRequest.roomId())
                .startTime(bookingRequest.startTime())
                .endTime(bookingRequest.endTime())
                .purpose(bookingRequest.purpose())
                .build();

        // Persist the product, used the @RequiredArgsConstructor to inject the BookingRepository
        bookingRepository.save(booking);

        return new BookingResponse(booking.getBookingId(), booking.getUserId(), booking.getRoomId(),
                booking.getStartTime(), booking.getEndTime(), booking.getPurpose());
    }

    @Override
    public List<BookingResponse> getAllBookings() {
        log.debug("Getting all bookings");
        List<Booking> bookings = bookingRepository.findAll();
//        return bookings.stream().map(booking -> mapToBookingResponse(booking)).toList();
        return bookings.stream().map(this::mapToBookingResponse).toList(); // same as the one above
    }

    private BookingResponse mapToBookingResponse(Booking booking) {
        return new BookingResponse(booking.getBookingId(), booking.getUserId(), booking.getRoomId(),
                booking.getStartTime(), booking.getEndTime(), booking.getPurpose());
    }

    @Override
    public String updateBooking(String bookingId, BookingRequest bookingRequest) {
        log.debug("Updating booking: {}", bookingId);

        Query query = new Query();
        query.addCriteria(Criteria.where("bookingId").is(bookingId));
        Booking booking = mongoTemplate.findOne(query, Booking.class);

        if(booking == null) {
            booking.setUserId(bookingRequest.userId());
            booking.setRoomId(bookingRequest.roomId());
            booking.setStartTime(bookingRequest.startTime());
            booking.setEndTime(bookingRequest.endTime());
            booking.setPurpose(bookingRequest.purpose());
            return bookingRepository.save(booking).getBookingId();
        }

        return bookingId;
    }

    @Override
    public void deleteBooking(String bookingId) {
        log.debug("Deleting booking: {}", bookingId);
        bookingRepository.deleteById(bookingId);
    }
}
