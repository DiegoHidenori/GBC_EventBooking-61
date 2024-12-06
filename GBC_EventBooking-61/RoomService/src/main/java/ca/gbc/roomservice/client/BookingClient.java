package ca.gbc.roomservice.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.time.LocalDateTime;

public interface BookingClient {

    @GetExchange("/api/booking/check-availability")
    boolean isRoomAvailable(@RequestParam Long roomId,
                            @RequestParam LocalDateTime startTime,
                            @RequestParam LocalDateTime endTime);
}
