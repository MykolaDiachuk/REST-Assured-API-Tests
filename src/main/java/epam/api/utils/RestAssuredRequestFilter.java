package epam.api.utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestAssuredRequestFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(RestAssuredRequestFilter.class);

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext filterContext) {
        sanitizeTokenInHeaders(requestSpec);

        Response response = filterContext.next(requestSpec, responseSpec);

        StringBuilder msg = new StringBuilder("\n")
                .append("%-20s %s \n".formatted("Request method:", requestSpec.getMethod()))
                .append("%-20s %s \n".formatted("Request URI:", requestSpec.getURI()))
                .append("Request Headers:\n").append(requestSpec.getHeaders()).append("\n");

        String body = extractRequestBody(requestSpec);
        msg.append("Request Body:\n").append(prettyPrint(body)).append("\n");

        msg.append("\n")
                .append("%-20s %s \n".formatted("Status:", response.statusLine()))
                .append("Response Body:\n").append(prettyPrint(response.getBody().asString())).append("\n");

        logger.info(msg.toString());

        return response;
    }

    private String extractRequestBody(FilterableRequestSpecification requestSpec) {
        if (requestSpec.getBody() != null) {
            return requestSpec.getBody().toString();
        }
        return "No body content";
    }

    private void sanitizeTokenInHeaders(FilterableRequestSpecification requestSpec) {
        String cookieHeader = requestSpec.getHeaders().getValue("Cookie");

        if (cookieHeader != null && cookieHeader.contains("token=")) {
            String sanitizedCookie = cookieHeader.replaceAll("(?<=token=)\\S+", "*****");
            requestSpec.header("Cookie", sanitizedCookie);
        }
    }

    private String prettyPrint(String responseBody) {
        try {
            return JsonUtils.prettyPrint(responseBody);
        } catch (Exception e) {
            return responseBody;
        }
    }
}
