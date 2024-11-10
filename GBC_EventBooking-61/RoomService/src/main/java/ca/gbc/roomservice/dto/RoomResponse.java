package ca.gbc.roomservice.dto;

public record RoomResponse(
        Long roomId,
        String roomName,
        int capacity,
        String features,
        boolean available
) {}
