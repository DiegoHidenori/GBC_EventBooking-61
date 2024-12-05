package ca.gbc.bookingservice.service;

import ca.gbc.bookingservice.client.RoomClient;
import ca.gbc.bookingservice.client.UserClient;
import ca.gbc.bookingservice.dto.BookingRequest;
import ca.gbc.bookingservice.dto.BookingResponse;
import ca.gbc.bookingservice.exception.RoomNotAvailableException;
import ca.gbc.bookingservice.exception.UserNotFoundException;
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

        Long userId = bookingRequest.userId();
        Long roomId = bookingRequest.roomId();

        log.info("Checking room with ID: {} exists.", roomId);
        if (!roomClient.roomExists(roomId)) {
            log.error("Room with ID {} does not exist", roomId);
            throw new IllegalArgumentException("Room with ID " + roomId + " does not exist");
        }

        log.info("Checking user with ID: {} exists.", userId);
        if (!userClient.userExists(userId)) {
            log.error("User with ID {} does not exist", userId);
            throw new UserNotFoundException("User with ID " + userId + " does not exist");
        }

        log.info("Checking for overlapping bookings for room ID: {}", roomId);
        if (isRoomDoubleBooked(bookingRequest.roomId(), bookingRequest.startTime(), bookingRequest.endTime())) {
            log.error("Room is already booked for the selected time range.");
            throw new RoomNotAvailableException("Room is already booked for the selected time range.");
        }

        log.info("Checking room availability for booking request: {}", bookingRequest);
        String startTime = bookingRequest.startTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String endTime = bookingRequest.endTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (!roomClient.isRoomAvailable(roomId, startTime, endTime)) {
            log.error("Room with ID {} is not available between {} and {}", roomId, startTime, endTime);
            throw new RuntimeException("Room is not available during the requested time.");
        }

        log.info("Creating booking entity");
        Booking booking = Booking.builder()
                .bookingId(bookingRequest.bookingId())
                .userId(bookingRequest.userId())
                .roomId(bookingRequest.roomId())
                .startTime(bookingRequest.startTime())
                .endTime(bookingRequest.endTime())
                .purpose(bookingRequest.purpose())
                .build();

        // Persist the product, used the @RequiredArgsConstructor to inject the BookingRepository
        log.info("Saving booking");
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

        Long userId = bookingRequest.userId();
        Long roomId = bookingRequest.roomId();

        log.info("Checking room with ID: {} exists.", roomId);
        if (!roomClient.roomExists(roomId)) {
            log.error("Room with ID {} does not exist", roomId);
            throw new IllegalArgumentException("Room with ID " + roomId + " does not exist");
        }

        log.info("Checking user with ID: {} exists.", userId);
        if (!userClient.userExists(userId)) {
            log.error("User with ID {} does not exist", userId);
            throw new UserNotFoundException("User with ID " + userId + " does not exist");
        }

        log.info("Checking for overlapping bookings for room ID: {}", roomId);
        if (isRoomDoubleBooked(bookingRequest.roomId(), bookingRequest.startTime(), bookingRequest.endTime())) {
            log.error("Room is already booked for the selected time range.");
            throw new RoomNotAvailableException("Room is already booked for the selected time range.");
        }

        log.info("Checking room availability for booking request: {}", bookingRequest);
        String startTime = bookingRequest.startTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String endTime = bookingRequest.endTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        if (!roomClient.isRoomAvailable(roomId, startTime, endTime)) {
            log.error("Room with ID {} is not available between {} and {}", roomId, startTime, endTime);
            throw new RuntimeException("Room is not available during the requested time.");
        }

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
    public boolean doesBookingExist(String bookingId) {
        log.info("Checking if booking exists with ID: {}", bookingId);
        return bookingRepository.existsById(bookingId);
    }

    @Override
    public void deleteBooking(String bookingId) {

        log.info("Attempting to delete booking with ID: {}", bookingId);
        bookingRepository.deleteById(bookingId);
        log.info("Booking with ID: {} deleted successfully", bookingId);

    }

//    @Override
//    public boolean checkRoomAvailability(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
//        String formattedStartTime = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//        String formattedEndTime = endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//        return roomClient.isRoomAvailable(roomId, formattedStartTime, formattedEndTime);
//    }

//    @Override
//    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
//        Query query = new Query(Criteria.where("roomId").is(roomId)
//                .orOperator(
//                        Criteria.where("startTime").lt(endTime).and("endTime").gt(startTime),
//                        Criteria.where("startTime").gte(startTime).and("startTime").lt(endTime)
//                )
//        );
//        long count = mongoTemplate.count(query, Booking.class);
//
//        // Room is available if no bookings overlap
//        return count == 0;
//    }
    @Override
    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        // Validate input times
        if (startTime.isAfter(endTime)) {
            log.warn("Invalid time range: startTime {} is after endTime {}", startTime, endTime);
            throw new IllegalArgumentException("Start time cannot be after end time");
        }

        log.info("Checking for overlapping bookings for room ID: {} from {} to {}", roomId, startTime, endTime);

        Query query = new Query();
        query.addCriteria(Criteria.where("roomId").is(roomId))
                .addCriteria(new Criteria().orOperator(
                        Criteria.where("startTime").lt(endTime).andOperator(Criteria.where("endTime").gt(startTime)), // Overlaps
                        Criteria.where("startTime").gte(startTime).andOperator(Criteria.where("endTime").lte(endTime)) // Contains range
                ));

        boolean isAvailable = !mongoTemplate.exists(query, Booking.class);

        log.info("Room availability for ID {}: {}", roomId, isAvailable);
        return isAvailable; // Room is available if no overlapping bookings are found
    }
}
