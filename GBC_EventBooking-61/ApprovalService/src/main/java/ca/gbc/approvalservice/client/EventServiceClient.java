package ca.gbc.approvalservice.client;

import ca.gbc.approvalservice.model.Event;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface EventServiceClient {
    @GetMapping("/api/events/{eventId}")
    Optional<Event> getEventById(@PathVariable String eventId);
}
