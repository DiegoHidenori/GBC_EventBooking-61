package ca.gbc.roomservice.config;

import ca.gbc.roomservice.client.BookingClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${booking.service.url}")
    private String bookingServiceUrl;

    @Bean
    public BookingClient bookingClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(bookingServiceUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(BookingClient.class);
    }


}
