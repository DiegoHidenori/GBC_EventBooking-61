package ca.gbc.notificationservice.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingPlacedEvent {

    private String bookingNumber;
    private String email;

}
