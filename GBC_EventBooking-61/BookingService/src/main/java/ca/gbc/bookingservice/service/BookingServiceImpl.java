package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.client.RoomClient;
import ca.gbc.bookingservice.client.UserClient;
import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.model.Booking;
import ca.gbc.bookingservice.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final MongoTemplate mongoTemplate;
    private final RestTemplate restTemplate;
    private final UserClient userClient;
    private final RoomClient roomClient;


    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {

        log.info("Processing booking request: {}", bookingRequest);
        log.debug("Starting to process booking request with details: {}", bookingRequest);

        Long userId = bookingRequest.userId();
        Long roomId = bookingRequest.roomId();

//        if (!isRoomAvailable(bookingRequest.roomId(), bookingRequest.startTime(), bookingRequest.endTime())) {
//            throw new IllegalArgumentException("Room is unavailable for the requested time range.");
//        }
        // Check if Room exists
        if (!roomClient.roomExists(roomId)) {
            throw new RuntimeException("Room with ID " + roomId + " does not exist");
        }

        // Check if User exists
        if (!userClient.userExists(userId)) {
            throw new RuntimeException("User with ID " + userId + " does not exist");
        }

        if (isRoomDoubleBooked(bookingRequest.roomId(), bookingRequest.startTime(), bookingRequest.endTime())) {
//            log.warn("Attempt to double book room ID: {} from {} to {}",
//                    bookingRequest.roomId(), bookingRequest.startTime(), bookingRequest.endTime());
            throw new IllegalArgumentException("Room is already booked for the selected time range.");
        }

        // Check Room Availability
        String startTime = bookingRequest.startTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String endTime = bookingRequest.endTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (!roomClient.isRoomAvailable(roomId, startTime, endTime)) {
            throw new RuntimeException("Room is not available during the requested time.");
        }


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

        log.info("Booking created successfully with ID: {}", booking.getBookingId());

        return mapToBookingResponse(booking);

    }

    private boolean isRoomDoubleBooked(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        log.debug("Checking for existing bookings for room ID: {} from {} to {}", roomId, startTime, endTime);

        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId))
                .addCriteria(new Criteria().orOperator(
                        Criteria.where("startTime").lt(endTime).andOperator(Criteria.where("endTime").gt(startTime)),  // Overlaps with the requested range
                        Criteria.where("startTime").lte(startTime).and("endTime").gte(endTime) // Existing booking completely covers requested range
                ));

        boolean isDoubleBooked = mongoTemplate.exists(query, Booking.class);
        log.debug("Double booking check result for room ID {}: {}", roomId, isDoubleBooked);
        return isDoubleBooked;
    }


    @Override
    public List<BookingResponse> getAllBookings() {

        log.info("Retrieving all bookings");
        List<Booking> bookings = bookingRepository.findAll();
        log.info("Total bookings retrieved: {}", bookings.size());

//        return bookings.stream().map(booking -> mapToBookingResponse(booking)).toList();
        return bookings.stream().map(this::mapToBookingResponse).toList(); // same as the one above

    }

    private BookingResponse mapToBookingResponse(Booking booking) {

        log.debug("Mapping booking entity to response for booking ID: {}", booking.getBookingId());

        return new BookingResponse(

                booking.getBookingId(),
                booking.getUserId(),
                booking.getRoomId(),
                booking.getStartTime(),
                booking.getEndTime(),
                booking.getPurpose());

    }

    @Override
    public String updateBooking(String bookingId, BookingRequest bookingRequest) {

        log.info("Attempting to update booking with ID: {}", bookingId);

        Query query = new Query();
        query.addCriteria(Criteria.where("bookingId").is(bookingId));
        Booking booking = mongoTemplate.findOne(query, Booking.class);

        if (booking == null) {
            log.warn("No booking found with ID: {}", bookingId);
            return bookingId;
        }

        booking.setUserId(bookingRequest.userId());
        booking.setRoomId(bookingRequest.roomId());
        booking.setStartTime(bookingRequest.startTime());
        booking.setEndTime(bookingRequest.endTime());
        booking.setPurpose(bookingRequest.purpose());

        String updatedBookingId = bookingRepository.save(booking).getBookingId();
        log.info("Booking with ID: {} updated successfully", updatedBookingId);

        return updatedBookingId;

    }

    @Override
    public void deleteBooking(String bookingId) {

        log.info("Attempting to delete booking with ID: {}", bookingId);
        bookingRepository.deleteById(bookingId);
        log.info("Booking with ID: {} deleted successfully", bookingId);

    }

    @Override
    public boolean checkRoomAvailability(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        String formattedStartTime = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String formattedEndTime = endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        return roomClient.isRoomAvailable(roomId, formattedStartTime, formattedEndTime);
    }

//    public boolean isRoomAvailable(String roomId, LocalDateTime startTime, LocalDateTime endTime) {
//        String url = ROOM_SERVICE_URL + "/" + roomId + "/availability?startTime=" + startTime + "&endTime=" + endTime;
//        return Boolean.TRUE.equals(restTemplate.getForObject(url, Boolean.class));
//    }
}
