package epam.api.tests.utils;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;

public class Authentication {
    private static final String BASE_URL = "https://restful-booker.herokuapp.com";
    private static LocalDateTime createdAt;
    private static String token;

    private static void retriveToken() {

        RestAssured.baseURI = BASE_URL;

        String requestBody = "{\n" +
                "    \"username\": \"admin\",\n" +
                "    \"password\": \"password123\"\n" +
                "}";

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
        if (token == null || isExpired()) retriveToken();
        return token;
    }

    private static boolean isExpired() {
        return createdAt.plusMinutes(5).isAfter(LocalDateTime.now());
    }

}
