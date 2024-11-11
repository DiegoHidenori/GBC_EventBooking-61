package ca.gbc.approvalservice.controller;

import ca.gbc.approvalservice.model.Approval;
import ca.gbc.approvalservice.model.Event;
import ca.gbc.approvalservice.repository.ApprovalRepository;
import ca.gbc.approvalservice.client.UserServiceClient;
import ca.gbc.approvalservice.client.EventServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/approve/{eventId}")
    public ResponseEntity<String> approveEvent(@PathVariable String eventId, @RequestParam String staffId) {
        if (!userServiceClient.isStaff(staffId)) {
            return new ResponseEntity<>("Only staff members can approve events", HttpStatus.FORBIDDEN);
        }

        Optional<Event> event = eventServiceClient.getEventById(eventId);
        if (event.isEmpty()) {
            return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
        }

        Approval approval = new Approval(eventId, staffId, true);
        approvalRepository.save(approval);
        return new ResponseEntity<>("Event approved successfully", HttpStatus.OK);
    }

    @PutMapping("/reject/{eventId}")
    public ResponseEntity<String> rejectEvent(@PathVariable String eventId, @RequestParam String staffId) {
        if (!userServiceClient.isStaff(staffId)) {
            return new ResponseEntity<>("Only staff members can reject events", HttpStatus.FORBIDDEN);
        }

        Optional<Event> event = eventServiceClient.getEventById(eventId);
        if (event.isEmpty()) {
            return new ResponseEntity<>("Event not found", HttpStatus.NOT_FOUND);
        }

        Approval approval = new Approval(eventId, staffId, false);
        approvalRepository.save(approval);
        return new ResponseEntity<>("Event rejected successfully", HttpStatus.OK);
    }

    @GetMapping
    public List<Approval> getAllApprovals() {
        return approvalRepository.findAll();
    }
}