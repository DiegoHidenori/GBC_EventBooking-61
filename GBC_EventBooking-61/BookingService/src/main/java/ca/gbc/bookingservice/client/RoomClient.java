package ca.gbc.bookingservice.client;

import ca.gbc.bookingservice.exception.FallbackException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.service.annotation.GetExchange;

public interface RoomClient {

    @GetExchange("/api/room/{roomId}/exists")
    boolean roomExists(@PathVariable Long roomId);

    @GetExchange("/api/room/{roomId}/availability")
//    @CircuitBreaker(name = "roomServiceCircuitBreaker", fallbackMethod = "fallbackIsRoomAvailable")
//    @Retry(name = "roomServiceCircuitBreaker")
    boolean isRoomAvailable(@PathVariable Long roomId,
                            @RequestParam String startTime,
                            @RequestParam String endTime);

//    default boolean fallbackRoomExists(Long roomId, Throwable ex) {
//        log.error("Fallback triggered. RoomService unavailable. Room ID: {}, Error: {}", roomId, ex.getMessage());
////        throw new FallbackException(
////                "RoomService is currently unavailable. Unable to verify if room exists for Room ID: " + roomId);
//        return false;
//    }
//
//    default boolean fallbackIsRoomAvailable(Long roomId, String startTime, String endTime, Throwable ex) {
//        log.error(
//                "Fallback triggered. RoomService unavailable. Room ID: {}, StartTime: {}, EndTime: {}, Error: {}",
//                roomId, startTime, endTime, ex.getMessage());
//        throw new FallbackException("RoomService is currently unavailable. Room ID: " + roomId
//                + " between " + startTime + " and " + endTime);
//    }

}
