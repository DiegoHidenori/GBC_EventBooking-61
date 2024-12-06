package ca.gbc.approvalservice.model;

import java.util.Arrays;

public enum ApprovalType {
    Approved,
    Pending,
    Rejected;

    public static boolean isValidType(String type) {
        return Arrays.stream(ApprovalType.values())
                .anyMatch(validType -> validType.name().equals(type));
    }
}
