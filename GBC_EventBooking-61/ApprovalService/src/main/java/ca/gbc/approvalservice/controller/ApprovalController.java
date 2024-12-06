package ca.gbc.approvalservice.controller;


import ca.gbc.approvalservice.dto.ApprovalRequest;
import ca.gbc.approvalservice.dto.ApprovalResponse;
import ca.gbc.approvalservice.service.ApprovalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/approval")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createApproval(@RequestBody ApprovalRequest approvalRequest) {

        log.info("Received approval request: {}", approvalRequest);
        ApprovalResponse approvalResponse = approvalService.createApproval(approvalRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/approval/" + approvalResponse.approvalId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .body(approvalResponse);

    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ApprovalResponse> getAllApprovals() {
        return approvalService.getAllApprovals();
    }

    @PutMapping("/{approvalId}")
    public ResponseEntity<?> updateApproval(
            @PathVariable String approvalId,
            @RequestBody ApprovalRequest approvalRequest) {

        String updatedApprovalId = approvalService.updateApproval(approvalId, approvalRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/approval/" + updatedApprovalId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .headers(headers)
                .build();

    }

    @DeleteMapping("/{approvalId}")
    public ResponseEntity<?> deleteApproval(@PathVariable String approvalId) {
        approvalService.deleteApproval(approvalId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
