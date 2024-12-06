package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.model.Approval;

import java.util.List;

public interface ApprovalService {

    ApprovalResponse createApproval(ApprovalRequest approvalRequest);
    List<ApprovalResponse> getAllApprovals();
    String updateApproval(String approvalId, ApprovalRequest approvalRequest);
    void deleteApproval(String approvalId);
//    List<Approval> getApprovalByEventId(String eventId);

}
