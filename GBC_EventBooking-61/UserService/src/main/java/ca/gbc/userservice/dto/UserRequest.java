package ca.gbc.userservice.dto;

public record UserRequest(
        Long userId,
        String name,
        String email,
        String role,
        String userType
) {}
