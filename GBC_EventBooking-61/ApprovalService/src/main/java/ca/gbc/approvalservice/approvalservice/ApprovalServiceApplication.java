package ca.gbc.approvalservice.approvalservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients  // Enables Feign client scanning and configuration
pkublic class ApprovalServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApprovalServiceApplication.class, args);
    }
}

