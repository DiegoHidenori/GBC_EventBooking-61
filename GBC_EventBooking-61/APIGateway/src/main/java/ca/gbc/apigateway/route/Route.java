package ca.gbc.apigateway.route;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

@Configuration
@Slf4j
public class Route {

    @Value("${approval-service.url}")
    private String approvalServiceUrl;

    @Value("${event-service.url}")
    private String eventServiceUrl;

    @Value("${booking-service.url}")
    private String bookingServiceUrl;

    @Value("${room-service.url}")
    private String roomServiceUrl;

    @Value("${user-service.url}")
    private String userServiceUrl;


    @Bean
    public RouterFunction<ServerResponse> approvalServiceRoute() {

        log.info("Initializing approval service route with URL: {}", approvalServiceUrl);
        return GatewayRouterFunctions.route("approval-service")
                .route(RequestPredicates.path("/api/approval/**"), request -> {

                    log.info("Request received for approval-service: {}", request.uri());

                    try{
                        ServerResponse response = HandlerFunctions.http(approvalServiceUrl).handle(request);
                        log.info("Response status: {}", response.statusCode());
                        return response;
                    } catch (Exception e) {
                        log.error("Error occurred while routing to: {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing to product-service");
                    }

                })
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> approvalServiceSwaggerRoute() {

        return GatewayRouterFunctions.route("approval_service_swagger")
                .route(RequestPredicates.path("/aggregate/approval-service/v3/api-docs"),
                        HandlerFunctions.http(approvalServiceUrl))
                .filter(setPath("/api-docs"))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> eventServiceRoute() {

        log.info("Initializing event service route with URL: {}", eventServiceUrl);
        return GatewayRouterFunctions.route("event-service")
                .route(RequestPredicates.path("/api/event/**"), request -> {

                    log.info("Request received for event-service: {}", request.uri());

                    try{
                        ServerResponse response = HandlerFunctions.http(eventServiceUrl).handle(request);
                        log.info("Response status: {}", response.statusCode());
                        return response;
                    } catch (Exception e) {
                        log.error("Error occurred while routing to: {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing to event-service");
                    }

                })
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> eventServiceSwaggerRoute() {

        return GatewayRouterFunctions.route("event_service_swagger")
                .route(RequestPredicates.path("/aggregate/event-service/v3/api-docs"),
                        HandlerFunctions.http(eventServiceUrl))
                .filter(setPath("/api-docs"))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> bookingServiceRoute() {

        log.info("Initializing booking service route with URL: {}", bookingServiceUrl);
        return GatewayRouterFunctions.route("booking-service")
                .route(RequestPredicates.path("/api/booking/**"), request -> {

                    log.info("Request received for booking-service: {}", request.uri());

                    try{
                        ServerResponse response = HandlerFunctions.http(bookingServiceUrl).handle(request);
                        log.info("Response status: {}", response.statusCode());
                        return response;
                    } catch (Exception e) {
                        log.error("Error occurred while routing to: {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing to event-service");
                    }

                })
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> bookingServiceSwaggerRoute() {

        return GatewayRouterFunctions.route("booking_service_swagger")
                .route(RequestPredicates.path("/aggregate/booking-service/v3/api-docs"),
                        HandlerFunctions.http(bookingServiceUrl))
                .filter(setPath("/api-docs"))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> roomServiceRoute() {

        log.info("Initializing room service route with URL: {}", roomServiceUrl);
        return GatewayRouterFunctions.route("room-service")
                .route(RequestPredicates.path("/api/room/**"), request -> {

                    log.info("Request received for room-service: {}", request.uri());

                    try{
                        ServerResponse response = HandlerFunctions.http(roomServiceUrl).handle(request);
                        log.info("Response status: {}", response.statusCode());
                        return response;
                    } catch (Exception e) {
                        log.error("Error occurred while routing to: {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing to event-service");
                    }

                })
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> roomServiceSwaggerRoute() {

        return GatewayRouterFunctions.route("room_service_swagger")
                .route(RequestPredicates.path("/aggregate/room-service/v3/api-docs"),
                        HandlerFunctions.http(roomServiceUrl))
                .filter(setPath("/api-docs"))
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {

        log.info("Initializing user service route with URL: {}", userServiceUrl);
        return GatewayRouterFunctions.route("user-service")
                .route(RequestPredicates.path("/api/user/**"), request -> {

                    log.info("Request received for user-service: {}", request.uri());

                    try{
                        ServerResponse response = HandlerFunctions.http(userServiceUrl).handle(request);
                        log.info("Response status: {}", response.statusCode());
                        return response;
                    } catch (Exception e) {
                        log.error("Error occurred while routing to: {}", e.getMessage(), e);
                        return ServerResponse.status(500).body("Error occurred when routing to event-service");
                    }

                })
                .build();

    }

    @Bean
    public RouterFunction<ServerResponse> userServiceSwaggerRoute() {

        return GatewayRouterFunctions.route("user_service_swagger")
                .route(RequestPredicates.path("/aggregate/user-service/v3/api-docs"),
                        HandlerFunctions.http(userServiceUrl))
                .filter(setPath("/api-docs"))
                .build();

    }

}
