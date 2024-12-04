package ca.gbc.roomservice.service;

import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomService {

    RoomResponse createRoom(RoomRequest roomRequest);
    List<RoomResponse> getAllRooms();
    RoomResponse updateRoom(Long roomId, RoomRequest roomRequest);
    void deleteRoom(Long roomId);
    boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime);
    RoomResponse getRoomById(Long roomId);

}
