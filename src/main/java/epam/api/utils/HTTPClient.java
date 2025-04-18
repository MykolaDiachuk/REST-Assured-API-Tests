package epam.api.utils;

import epam.api.services.AuthService;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class HTTPClient {

    private static RequestSpecification getBaseRequest() {
        return given().contentType(ContentType.JSON);
    }

    public static Response POST(String url, Object body) {
        RequestSpecification request = getBaseRequest().body(body);
        return executeRequest(Method.POST, url, request);
    }

    public static Response GET(String url) {
        RequestSpecification request = getBaseRequest();
        return executeRequest(Method.GET, url, request);
    }

    public static Response PUT(String url, Object body) {
        RequestSpecification request = getBaseRequest().body(body);
        return executeRequest(Method.PUT, url, request);
    }

    public static Response PATCH(String url, Object body) {
        RequestSpecification request = getBaseRequest().body(body);
        return executeRequest(Method.PATCH, url, request);
    }

    public static Response DELETE(String url) {
        RequestSpecification request = getBaseRequest()
                .header("Cookie", "token=" + AuthService.getAuthToken());
        return executeRequest(Method.DELETE, url, request);
    }

    private static Response executeRequest(Method method, String url, RequestSpecification rs) {
        return rs.request(method, url);
    }
}
