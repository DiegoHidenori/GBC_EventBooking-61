package ca.gbc.approvalservice.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface EventClient {

    @GetExchange("/api/event/{eventId}/exists")
    boolean eventExists(@PathVariable String eventId);

}