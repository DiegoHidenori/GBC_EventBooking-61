package ca.gbc.userservice.model;

import java.util.Arrays;

public enum UserType {
    Student,
    Faculty,
    Staff,
    Other;

    public static boolean isValidType(String type) {
        return Arrays.stream(UserType.values())
                .anyMatch(validType -> validType.name().equals(type));
    }

}
