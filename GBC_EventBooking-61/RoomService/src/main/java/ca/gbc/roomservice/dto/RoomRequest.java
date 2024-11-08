package ca.gbc.roomservice.dto;

public record RoomRequest(
        Long id,
        String roomName,
        int capacity,
        String features,
        String availability
) {}
