package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.model.Approval;

import java.util.List;
import java.util.Optional;

public interface ApprovalService {
    Approval approveEvent(String eventId, String staffId);
    Approval rejectEvent(String eventId, String staffId);
    List<Approval> getAllApprovals();
    Optional<Approval> getApprovalById(Long id);
}
