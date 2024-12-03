package ca.gbc.userservice.dto;

import ca.gbc.userservice.model.UserType;

public record UserResponse(
        Long userId,
        String name,
        String email,
        String role,
        String userType
) {}
