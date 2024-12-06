package ca.gbc.approvalservice.service;

import ca.gbc.approvalservice.client.EventClient;
import ca.gbc.approvalservice.client.UserClient;
import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.exception.ApprovalAlreadyExistsException;
import ca.gbc.approvalservice.exception.EventNotFoundException;
import ca.gbc.approvalservice.exception.UserNotFoundException;
import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.model.ApprovalType;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApprovalServiceImpl implements ApprovalService {

    private final ApprovalRepository approvalRepository;
    private final MongoTemplate mongoTemplate;
    private final UserClient userClient;
    private final EventClient eventClient;

    @Override
    public ApprovalResponse createApproval(ApprovalRequest approvalRequest) {

        log.info("Processing approval request: {}", approvalRequest);

        String eventId = approvalRequest.eventId();
        log.info("Checking if approval already exists for event ID: {}", eventId);
        Query query = new Query();
        query.addCriteria(Criteria.where("eventId").is(eventId));

        if (mongoTemplate.exists(query, Approval.class)) {
            log.error("Approval already exists for event ID: {}", eventId);
            throw new ApprovalAlreadyExistsException("Approval already exists for event ID: " + eventId);
        }

        if (!ApprovalType.isValidType(approvalRequest.status())) {
            log.info("Invalid approval type");
            throw new IllegalArgumentException("Invalid approval type: " + approvalRequest.status());
        }

        Long userId = approvalRequest.reviewerId();

        log.info("Checking user with ID: {} exists.", userId);
        if (!userClient.userExists(userId)) {
            log.error("User with ID {} does not exist", userId);
            throw new UserNotFoundException("User with ID " + userId + " does not exist");
        }

        String userType = userClient.getUserType(userId);

        log.info("Checking event with ID: {} exists.", eventId);
        if (!eventClient.eventExists(eventId)) {
            log.error("Event with ID {} does not exist", eventId);
            throw new EventNotFoundException("Event with ID " + eventId + " does not exist");
        }

        log.info("Checking userType of user with ID: {}.", userId);
        if (!"Staff".equalsIgnoreCase(userType)) {
            log.error("Only staff can approve/reject events.");
            throw new IllegalArgumentException("Only staff can approve/reject events.");
        }

        Approval approval = Approval.builder()
                .approvalId(approvalRequest.approvalId())
                .eventId(approvalRequest.eventId())
                .reviewerId(approvalRequest.reviewerId())
                .status(approvalRequest.status())
                .remarks(approvalRequest.remarks())
                .build();

        log.info("Saving approval {}", approval);
        approvalRepository.save(approval);

        log.info("Approval created successfully with ID: {}", approval.getApprovalId());
        return mapToApprovalResponse(approval);
    }

    private ApprovalResponse mapToApprovalResponse(Approval approval) {

        log.debug("Mapping approval entity to response for approval ID: {}", approval.getApprovalId());

        return new ApprovalResponse(

                approval.getApprovalId(),
                approval.getEventId(),
                approval.getReviewerId(),
                approval.getStatus(),
                approval.getRemarks());

    }

    @Override
    public List<ApprovalResponse> getAllApprovals() {
        log.info("Getting all approvals");
        List<Approval> approvals = approvalRepository.findAll();
        log.info("Total approvals retrieved: {}", approvals.size());

        return approvals.stream().map(this::mapToApprovalResponse).toList();
    }

    @Override
    public String updateApproval(String approvalId, ApprovalRequest approvalRequest) {
        log.info("Attempting to update approval with ID: {}", approvalId);

        Long userId = approvalRequest.reviewerId();
        String userType = userClient.getUserType(userId);
        String eventId = approvalRequest.eventId();

        log.info("Checking user with ID: {} exists.", userId);
        if (!userClient.userExists(userId)) {
            log.error("User with ID {} does not exist", userId);
            throw new UserNotFoundException("User with ID " + userId + " does not exist");
        }

        log.info("Checking event with ID: {} exists.", eventId);
        if (!eventClient.eventExists(eventId)) {
            log.error("Event with ID {} does not exist", eventId);
            throw new EventNotFoundException("Event with ID " + eventId + " does not exist");
        }

        log.info("Checking userType of user with ID: {}.", userId);
        if (!"Staff".equalsIgnoreCase(userType)) {
            log.error("Only staff can approve/reject events.");
            throw new IllegalArgumentException("Only staff can approve/reject events.");
        }

        Query query = new Query();
        query.addCriteria(Criteria.where("approvalId").is(approvalId));
        Approval approval = mongoTemplate.findOne(query, Approval.class);

        if (approval == null) {
            log.warn("No approval found with ID: {}", approvalId);
            return "Approval not found" + approvalId;
        }

        /*
        Approval approval = Approval.builder()
                .approvalId(approvalRequest.approvalId())
                .eventId(approvalRequest.eventId())
                .reviewerId(approvalRequest.reviewerId())
                .status(approvalRequest.status())
                .remarks(approvalRequest.remarks())
                .build();

        log.info("Saving approval {}", approval);
        approvalRepository.save(approval);

        log.info("Approval created successfully with ID: {}", approval.getApprovalId());
        return mapToApprovalResponse(approval);
         */

        approval.setEventId(approvalRequest.eventId());
        approval.setReviewerId(approvalRequest.reviewerId());
        approval.setStatus(approvalRequest.status());
        approval.setRemarks(approvalRequest.remarks());

        String updatedApprovalId = approvalRepository.save(approval).getApprovalId();
        log.info("Approval with ID: {} updated successfully", updatedApprovalId);

        return updatedApprovalId;

//        // Verify reviewer role
//        String role = userClient.getUserRole(reviewerId);
//        if (!role.equalsIgnoreCase("STAFF")) {
//            throw new IllegalArgumentException("Reviewer does not have sufficient privileges.");
//        }
//
//        // Fetch approval record
//        Approval approval = approvalRepository.findById(approvalId)
//                .orElseThrow(() -> new IllegalArgumentException("Approval not found"));
//
//        // Update approval record
//        approval.setStatus(status);
//        approval.setReviewerId(reviewerId);
//        approval.setRemarks(remarks);
//
//        return approvalRepository.save(approval);
    }

    public void deleteApproval(String approvalId) {
        log.info("Deleting approval ID: {}", approvalId);
        approvalRepository.deleteById(approvalId);
        log.info("Approval with ID: {} deleted successfully", approvalId);

    }

//    @Override
//    public List<Approval> getApprovalByEventId(String eventId) {
//        return approvalRepository.findByEventId(eventId);
//    }

}
