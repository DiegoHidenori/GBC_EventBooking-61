package ca.gbc.approvalservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClent(name = "user-service")
public interface UserServiceClient {
    @GetMapping("/api/users/is-staff")
    boolean isStaff(@RequestParam String staffId);
}
