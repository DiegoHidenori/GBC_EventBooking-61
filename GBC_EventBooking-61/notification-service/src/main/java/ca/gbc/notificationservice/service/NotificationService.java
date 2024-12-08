package ca.gbc.notificationservice.service;


import ca.gbc.notificationservice.event.BookingPlacedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender javaMailSender;

    @KafkaListener(topics = "booking-placed")
    public void listen(BookingPlacedEvent bookingPlacedEvent) {

        log.info("Received message from booking-placed topic {}", bookingPlacedEvent);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("comp3095@georgebrown.ca");
            messageHelper.setTo(bookingPlacedEvent.getEmail());
            messageHelper.setSubject(String.format("Your Booking (%s) was placed successfully",
                    bookingPlacedEvent.getBookingNumber()));
            messageHelper.setText(String.format("""
                    
                    Good Day.
                    
                    Your booking with booking number %s was successfully placed
                    
                    Thank you,
                    COMP3095
                    
                    """, bookingPlacedEvent.getBookingNumber()

            ));
        };

        try {

            javaMailSender.send(messagePreparator);
            log.info("Booking notification successfully sent");

        } catch (MailException e) {
            log.error("Error occurred when sending email", e);
            throw new RuntimeException("Exception occurred when trying to send email", e);
        }

    }
}
