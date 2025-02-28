package epam.api.tests.utils;

import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class HTTPClient {
    public static Response POST(String url, Object body) {
        RequestSpecification deleteRequest = given()
                .contentType(ContentType.JSON)
                .body(body);
        return  executeRequest(Method.POST,url,deleteRequest);
    }

    public static Response GET(String url, int statusCode) {
        RequestSpecification request = given()
                .contentType(ContentType.JSON);
        Response response = executeRequest(Method.GET, url, request);
        response.then().statusCode(statusCode);
        return response;
    }

    public static Response PUT(String url, Object body) {
        RequestSpecification deleteRequest = given()
                .contentType(ContentType.JSON)
                .body(body);
        return  executeRequest(Method.PUT,url,deleteRequest);
    }

    public static Response PATCH(String url, Object body) {
        RequestSpecification deleteRequest = given()
                .contentType(ContentType.JSON)
                .body(body);
        return executeRequest(Method.PATCH,url,deleteRequest);
    }

    public static Response DELETE(String url) {
        RequestSpecification deleteRequest = given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + Authentication.getAuthToken());
        return executeRequest(Method.DELETE, url, deleteRequest);

    }

    private static Response executeRequest(Method method, String url, RequestSpecification rs) {
        return rs.request(method, url);
    }

}
