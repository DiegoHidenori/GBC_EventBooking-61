package ca.gbc.eventservice.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface BookingClient {

    @GetExchange("/api/booking/{bookingId}/exists")
    boolean bookingExists(@PathVariable String bookingId);

}