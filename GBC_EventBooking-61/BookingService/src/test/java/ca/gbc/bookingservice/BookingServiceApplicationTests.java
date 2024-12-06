package ca.gbc.bookingservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;



@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingServiceApplicationTests {

	// Used in combination with TestContainers to automatically configure the connection to the Test MongoDbContainer
//	@ServiceConnection
//	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");
//
//	@LocalServerPort
//	private Integer port;
//
//
//	// Set up localhost and port
//	@BeforeEach
//	void setup(){
//
//		RestAssured.baseURI = "http://localhost";
//		RestAssured.port = port;
//
//	}
//
//	static {
//		mongoDBContainer.start();
//	}
//
//	@Test
//	void contextLoads() {
//	}
//
//	@Test
//	void createBookingTest(){
//		String requestBody = """
//				{
//					"userId": "test user",
//					"roomId": "test room id",
//					"startTime": "2024-11-12T10:00:00",
//					"endTime": "2024-11-12T11:00:00",
//					"purpose": "to test"
//				}
//				""";
//
//		RestAssured.given().contentType("application/json").body(requestBody)
//				.when().post("/api/booking")
//				.then().log().all().statusCode(201)
//				.body("bookingId", Matchers.notNullValue())
//				.body("userId", Matchers.equalTo("test user"))
//				.body("roomId", Matchers.equalTo("test room id"))
//				.body("startTime", Matchers.equalTo("2024-11-12T10:00:00"))
//				.body("endTime", Matchers.equalTo("2024-11-12T11:00:00"))
//				.body("purpose", Matchers.equalTo("to test"));
//
//	}
//
//	@Test
//	void getAllBookingsTest(){
//		String requestBody = """
//				{
//					"userId": "test user",
//					"roomId": "test room id",
//					"startTime": "2024-11-12T10:00:00",
//					"endTime": "2024-11-12T11:00:00",
//					"purpose": "to test"
//				}
//				""";
//
//		RestAssured.given().contentType("application/json").body(requestBody)
//				.when().post("/api/booking")
//				.then().log().all().statusCode(201)
//				.body("bookingId", Matchers.notNullValue())
//				.body("userId", Matchers.equalTo("test user"))
//				.body("roomId", Matchers.equalTo("test room id"))
//				.body("startTime", Matchers.equalTo("2024-11-12T10:00:00"))
//				.body("endTime", Matchers.equalTo("2024-11-12T11:00:00"))
//				.body("purpose", Matchers.equalTo("to test"));
//
//
//		RestAssured.given().contentType("application/json")
//				.when().get("/api/booking")
//				.then().log().all().statusCode(200)
//				.body("size()", Matchers.greaterThan(0))
//				.body("[0].userId", Matchers.equalTo("test user"))
//				.body("[0].roomId", Matchers.equalTo("test room id"))
//				.body("[0].startTime", Matchers.equalTo("2024-11-12T10:00:00"))
//				.body("[0].endTime", Matchers.equalTo("2024-11-12T11:00:00"))
//				.body("[0].purpose", Matchers.equalTo("to test"));
//	}
//
//	@Test
//	void updateBookingTest(){
//		// Create the initial booking
//		String initialRequestBody = """
//            {
//                "userId": "test user",
//                "roomId": "test room id",
//                "startTime": "2024-11-12T10:00:00",
//                "endTime": "2024-11-12T11:00:00",
//                "purpose": "initial purpose"
//            }
//            """;
//
//		String bookingId = RestAssured.given().contentType("application/json").body(initialRequestBody)
//				.when().post("/api/booking")
//				.then().statusCode(201)
//				.extract().path("bookingId");
//
//		// Define the update request body
//		String updateRequestBody = """
//            {
//                "userId": "test user",
//                "roomId": "test room id",
//                "startTime": "2024-11-12T12:00:00",
//                "endTime": "2024-11-12T13:00:00",
//                "purpose": "updated purpose"
//            }
//            """;
//
//		// Perform the update
//		RestAssured.given().contentType("application/json").body(updateRequestBody)
//				.when().put("/api/booking/" + bookingId)
//				.then().log().all().statusCode(204)
//				.body("bookingId", Matchers.equalTo(bookingId))
//				.body("purpose", Matchers.equalTo("updated purpose"));
//	}
//
//	@Test
//	void deleteBookingTest(){
//		// Create a booking to delete
//		String requestBody = """
//            {
//                "userId": "test user",
//                "roomId": "test room id",
//                "startTime": "2024-11-12T10:00:00",
//                "endTime": "2024-11-12T11:00:00",
//                "purpose": "to delete"
//            }
//            """;
//
//		String bookingId = RestAssured.given().contentType("application/json").body(requestBody)
//				.when().post("/api/booking")
//				.then().statusCode(201)
//				.extract().path("bookingId");
//
//		// Delete the booking
//		RestAssured.given().contentType("application/json")
//				.when().delete("/api/booking/" + bookingId)
//				.then().log().all().statusCode(204);
//
//		// Verify the booking is deleted by checking the GET endpoint
//		RestAssured.given().contentType("application/json")
//				.when().get("/api/booking/" + bookingId)
//				.then().log().all().statusCode(404);
//	}

}
