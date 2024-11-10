package ca.gbc.userservice.dto;

public record UserResponse(
        Long userId,
        String name,
        String email,
        String role,
        String userType
) {}
