package ca.gbc.approvalservice.exception;

public class ApprovalAlreadyExistsException extends RuntimeException {
    public ApprovalAlreadyExistsException(String message) {
        super(message);
    }
}
