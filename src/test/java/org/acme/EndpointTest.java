package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.github.tomakehurst.wiremock.client.WireMock;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@Slf4j
@QuarkusTestResource(WireMockTestResource.class)
public class EndpointTest {

    private static String wireMockUrl;

    @BeforeAll
    static void setup() {

        wireMockUrl = WireMockTestResource.getWireMockServer().baseUrl();
        log.info("✅ WireMock URL: " + wireMockUrl);

        WireMock.configureFor("localhost", WireMockTestResource.getWireMockServer().port());

        stubFor(get(urlEqualTo("/mocked"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Hello from WireMock!\"}")
                        .withStatus(200)));
    }

    @BeforeEach
    void beforeEach() {
        RestAssured.baseURI = wireMockUrl;
        log.info("✅ Тест использует RestAssured.baseURI: " + RestAssured.baseURI);
    }

    @Test
    void testMockedEndpoint() {
        log.info("✅ Выполняем запрос к: " + RestAssured.baseURI + "/mocked");
        given()
                .when()
                .get("/mocked")
                .then()
                .statusCode(200)
                .body("message", is("Hello from WireMock!"));
    }
}
