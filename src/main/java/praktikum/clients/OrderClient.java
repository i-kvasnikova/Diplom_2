package praktikum.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.responses.OrderListResponse;

import java.util.ArrayList;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderClient  extends StellarBurgersRestClient {

    @Step("Получить 50 последних заказов")
    public Response getAll() {
        return given()
                .spec(getBaseSpecification())
                .when()
                .get(apiConfig.getOrdersAllPath())
                .then()
                .extract().response();
    }

    @Step("Получить 50 последних заказов пользователя")
    public Response getAllPerUser(String userToken) {
        return given()
                .spec(getBaseSpecification())
                .auth().oauth2(userToken)
                .when()
                .get(apiConfig.getOrdersPerUserPath())
                .then()
                .extract().response();
    }

    public OrderListResponse getMappedOrdersPerUser(String userToken) {
      return getAllPerUser(userToken).getBody().as(OrderListResponse.class);
    }

    @Step("Создать заказ")
    public Response create(Map<String, ArrayList<String>> orderData, String userToken) {
        return given()
                .spec(getBaseSpecification())
                .auth().oauth2(userToken)
                .body(orderData)
                .when()
                .post(apiConfig.getOrdersPerUserPath())
                .then()
                .extract().response();
    }
}
