package praktikum.clients;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;
import praktikum.configuration.ApiConfig;

public class StellarBurgersRestClient {
    static ApiConfig apiConfig = ConfigFactory.create(ApiConfig.class);

    protected RequestSpecification getBaseSpecification() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(apiConfig.getBaseUrl())
                .build();
    }
}
