package ca.gbc.roomservice.service;

import ca.gbc.roomservice.client.BookingClient;
import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import ca.gbc.roomservice.exception.ResourceNotFoundException;
import ca.gbc.roomservice.model.Room;
import ca.gbc.roomservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final BookingClient bookingClient;

    @Override
    public RoomResponse createRoom(RoomRequest roomRequest) {

        log.info("Processing room request: {}", roomRequest);
        log.debug("Starting to process room request with details: {}", roomRequest);

        Room room = Room.builder()
                .roomId(roomRequest.roomId())
                .roomName(roomRequest.roomName())
                .capacity(roomRequest.capacity())
                .features(roomRequest.features())
                .available(roomRequest.available())
                .build();

        roomRepository.save(room);

        log.info("Room created successfully with ID: {}", room.getRoomId());

        return new RoomResponse(
                room.getRoomId(),
                room.getRoomName(),
                room.getCapacity(),
                room.getFeatures(),
                room.isAvailable()
        );

    }

    @Override
    public List<RoomResponse> getAllRooms() {

        log.info("Retrieving all rooms");
        log.debug("Fetching all rooms from the database");

        List<Room> rooms = roomRepository.findAll();
        log.info("Total rooms retrieved: {}", rooms.size());

        return rooms.stream().map(this::mapToRoomResponse).toList();
    }

    private RoomResponse mapToRoomResponse(Room room) {

        log.debug("Mapping room entity to response for room ID: {}", room.getRoomId());

        return new RoomResponse(

                room.getRoomId(),
                room.getRoomName(),
                room.getCapacity(),
                room.getFeatures(),
                room.isAvailable());

    }

    @Override
    public RoomResponse updateRoom(Long roomId, RoomRequest roomRequest) {
        log.info("Attempting to update room with ID: {}", roomId);
        log.debug("Attempting to update room with ID: {}", roomId);


        // Find the room by ID
        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isEmpty()) {
            log.warn("Room with ID {} not found", roomId);
            throw new IllegalArgumentException("Room not found");
        }

        Room room = optionalRoom.get();
        room.setRoomName(roomRequest.roomName());
        room.setCapacity(roomRequest.capacity());
        room.setFeatures(roomRequest.features());
        room.setAvailable(roomRequest.available());

        // Save the updated room to the database
        Room updatedRoom = roomRepository.save(room);
        log.info("Room with ID: {} updated successfully", updatedRoom.getRoomId());

        return mapToRoomResponse(updatedRoom);
    }

    @Override
    public void deleteRoom(Long roomId) {

        log.info("Attempting to delete room with ID: {}", roomId);
        log.debug("Attempting to delete room with ID: {}", roomId);

        if (!roomRepository.existsById(roomId)) {
            log.warn("Room with ID {} not found for deletion", roomId);
            throw new ResourceNotFoundException("Room not found with ID: " + roomId);
        }
        log.info("Room with ID: {} deleted successfully", roomId);
        roomRepository.deleteById(roomId);

    }

//    @Override
//    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
//        try {
//            String formattedStartTime = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//            String formattedEndTime = endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//            return bookingClient.isRoomAvailable(roomId, formattedStartTime, formattedEndTime);
//        } catch (Exception e) {
//            log.error("Failed to connect to BookingService: {}", e.getMessage());
//            return false;
//        }
//
//    }
    @Override
    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        // Validate input times
        if (startTime.isAfter(endTime)) {
            log.warn("Invalid time range: startTime {} is after endTime {}", startTime, endTime);
            throw new IllegalArgumentException("Start time cannot be after end time");
        }

        // Check if the room exists
        if (!doesRoomExist(roomId)) {
            log.error("Room with ID {} does not exist", roomId);
            throw new IllegalArgumentException("Invalid Room ID: Room does not exist");
        }

        try {
            log.info("Checking room availability with BookingService for room ID: {}", roomId);

            String formattedStartTime = startTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            String formattedEndTime = endTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

            // Use BookingClient to call BookingService
            boolean isAvailable = bookingClient.isRoomAvailable(roomId, formattedStartTime, formattedEndTime);

            log.info("Room availability for ID {}: {}", roomId, isAvailable);
            return isAvailable;

        } catch (Exception e) {
            log.error("Failed to connect to BookingService: {}", e.getMessage());
            return false; // Default to unavailable if BookingService fails
        }
    }


    @Override
    public RoomResponse getRoomById(Long roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with ID: " + roomId));
        return mapToRoomResponse(room);
    }

    @Override
    public boolean doesRoomExist(Long roomId) {
        return roomRepository.existsById(roomId);
    }


}
