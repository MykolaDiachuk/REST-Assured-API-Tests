package epam.api.services;
import epam.api.entities.AuthRequestDTO;
import io.restassured.response.Response;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;

public class AuthService {
    private static LocalDateTime createdAt;
    private static String token;

    private static void retrieveToken() {
        AuthRequestDTO requestBody = new AuthRequestDTO("admin", "password123");
        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .post("/auth")
                .then()
                .statusCode(200)
                .extract().response();

        createdAt = LocalDateTime.now();
        token =  response.jsonPath().getString("token");
    }

    public static String getAuthToken(){
        if (token == null || isExpired()) retrieveToken();
        return token;
    }

    private static boolean isExpired() {
        return createdAt.plusMinutes(5).isAfter(LocalDateTime.now());
    }

}
