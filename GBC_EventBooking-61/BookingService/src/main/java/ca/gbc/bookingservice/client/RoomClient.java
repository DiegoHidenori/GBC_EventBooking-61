package ca.gbc.bookingservice.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface RoomClient {

    @GetExchange("/api/room")
    boolean roomExists(@RequestParam Long roomId);

    @GetExchange("/api/room/{roomId}/availability")
    boolean isRoomAvailable(@PathVariable Long roomId,
                            @RequestParam String startTime,
                            @RequestParam String endTime);

}
