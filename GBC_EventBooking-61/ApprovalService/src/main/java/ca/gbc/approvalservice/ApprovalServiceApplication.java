package com.example.approvalservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/approvals")
public class ApprovalController {

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private EventServiceClient eventServiceClient;

    // Endpoint to approve an event
    @PutMapping("/approve/{eventId}")
    public ResponseEntity<String> approveEvent(@PathVariable String eventId, @RequestParam String staffId) {
        if (!userServiceClient.isStaff(staffId)) {
            return new ResponseEntity<>("Only staff members can approve events", HttpStatus.FORBIDDEN);
        }

        Optional<Event> event = eventServiceClient.getEventById(eventId);
        if (event.isEmpty()) {
            return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
        }

        // Create and save the approval
        Approval approval = new Approval(eventId, staffId, true);
        approvalRepository.save(approval);
        return new ResponseEntity<>("Event approved successfully", HttpStatus.OK);
    }

    // Endpoint to reject an event
    @PutMapping("/reject/{eventId}")
    public ResponseEntity<String> rejectEvent(@PathVariable String eventId, @RequestParam String staffId) {
        if (!userServiceClient.isStaff(staffId)) {
            return new ResponseEntity<>("Only staff members can reject events", HttpStatus.FORBIDDEN);
        }

        Optional<Event> event = eventServiceClient.getEventById(eventId);
        if (event.isEmpty()) {
            return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
        }

        // Create and save the rejection
        Approval approval = new Approval(eventId, staffId, false);
        approvalRepository.save(approval);
        return new ResponseEntity<>("Event rejected successfully", HttpStatus.OK);
    }

    // Endpoint to get all approvals
    @GetMapping
    public List<Approval> getAllApprovals() {
        return approvalRepository.findAll();
    }
}
