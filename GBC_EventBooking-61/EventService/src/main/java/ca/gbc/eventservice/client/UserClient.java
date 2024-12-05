package ca.gbc.eventservice.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface UserClient {

    @GetExchange("/api/user/{userId}/exists")
    boolean userExists(@PathVariable Long userId);

    @GetExchange("/api/user/{userId}/type")
    String getUserType(@PathVariable Long userId);

}