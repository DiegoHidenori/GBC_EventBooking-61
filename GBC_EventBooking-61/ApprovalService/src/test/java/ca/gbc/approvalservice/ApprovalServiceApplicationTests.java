package com.example.approvalservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApprovalServiceApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void contextLoads() {
        // Test if the Spring application context loads successfully
        assertThat(restTemplate).isNotNull();
    }

    @Test
    public void testApproveEvent() {
        // Mock data for testing
        String eventId = "123";
        String staffId = "staff001";
        String url = "/api/approvals/approve/" + eventId + "?staffId=" + staffId;

        // Send request to the approve endpoint
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, HttpEntity.EMPTY, String.class);

        // Verify the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Event approved successfully");
    }

    @Test
    public void testRejectEvent() {
        // Mock data for testing
        String eventId = "456";
        String staffId = "staff002";
        String url = "/api/approvals/reject/" + eventId + "?staffId=" + staffId;

        // Send request to the reject endpoint
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, HttpEntity.EMPTY, String.class);

        // Verify the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Event rejected successfully");
    }

    @Test
    public void testGetAllApprovals() {
        // Send request to get all approvals
        ResponseEntity<Approval[]> response = restTemplate.getForEntity("/api/approvals", Approval[].class);

        // Verify the response
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}

