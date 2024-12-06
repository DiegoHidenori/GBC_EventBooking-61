package ca.gbc.approvalservice.dto;

public record ApprovalResponse(

        String approvalId,
        String eventId,
        Long reviewerId,
        String status,
        String remarks

) {}
/*
    private String approvalId;

    private String eventId;
    private Long reviewerId;
    private String status;
    private String remarks;
     */