package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import ca.gbc.roomservice.model.Room;
import ca.gbc.roomservice.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

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

        if (roomRepository.existsById(roomId)) {
            roomRepository.deleteById(roomId);
            log.info("Room with ID: {} deleted successfully", roomId);
        } else {
            log.warn("Room with ID {} not found for deletion", roomId);
            throw new IllegalArgumentException("Room not found");
        }

    }

    @Override
    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        log.info("Checking availability for room ID: {}", roomId);

        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isPresent()) {
            boolean available = optionalRoom.get().isAvailable();
            log.info("Room ID: {} availability status: {}", roomId, available);
            return available;
        } else {
            log.warn("Room with ID {} not found for availability check", roomId);
            throw new IllegalArgumentException("Room not found");
        }
    }


}
