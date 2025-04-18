package ca.gbc.roomservice.dto;

public record RoomRequest(
        Long roomId,
        String roomName,
        int capacity,
        String features,
        boolean available
) {}
