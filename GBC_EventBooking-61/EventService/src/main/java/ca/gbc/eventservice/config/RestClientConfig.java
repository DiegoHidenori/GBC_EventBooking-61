package ca.gbc.eventservice.config;

import ca.gbc.eventservice.client.BookingClient;
import ca.gbc.eventservice.client.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${booking-service.url}")
    private String bookingServiceUrl;

    @Value("${user-service.url}")
    private String userServiceUrl;

    @Bean
    public BookingClient bookingClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(bookingServiceUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(BookingClient.class);
    }

    @Bean
    public UserClient userClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(userServiceUrl)
                .build();

        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(UserClient.class);
    }
}