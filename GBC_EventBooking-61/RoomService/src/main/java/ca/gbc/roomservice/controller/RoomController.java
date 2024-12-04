package ca.gbc.roomservice.controller;


import ca.gbc.roomservice.dto.RoomRequest;
import ca.gbc.roomservice.dto.RoomResponse;
import ca.gbc.roomservice.repository.RoomRepository;
import ca.gbc.roomservice.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/room")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest) {

        RoomResponse createdRoom = roomService.createRoom(roomRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/room/" + createdRoom.roomId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdRoom);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RoomResponse> getAllRooms() {
        return roomService.getAllRooms();
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<?> updateRoom(@PathVariable("roomId") Long roomId,
                                        @RequestBody RoomRequest roomRequest) {

        RoomResponse updatedRoom = roomService.updateRoom(roomId, roomRequest);

        return ResponseEntity.ok(updatedRoom);
    }

    @GetMapping("/{roomId}/availability")
    public ResponseEntity<Boolean> checkRoomAvailability(@PathVariable Long roomId,
                                                         @RequestParam LocalDateTime startTime,
                                                         @RequestParam LocalDateTime endTime) {
        LocalDateTime start = startTime;
        LocalDateTime end = endTime;
        boolean isAvailable = roomService.isRoomAvailable(roomId, startTime, endTime);
        return ResponseEntity.ok(isAvailable);
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<?> deleteRoom(@PathVariable("roomId") Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long roomId) {
        RoomResponse roomResponse = roomService.getRoomById(roomId);
        return ResponseEntity.ok(roomResponse);
    }

}
