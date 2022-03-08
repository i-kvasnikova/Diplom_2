package order_tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.clients.OrderClient;
import praktikum.clients.UserClient;
import praktikum.configuration.ErrorMessageConstants;
import praktikum.generators.OrderGenerator;
import praktikum.generators.UserGenerator;
import praktikum.responses.OrderListResponse;
import praktikum.responses.StellarburgersGeneralResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

@Epic("Тестирование метода создания заказа")
public class OrderCreateTest {

    String accessToken;
    ArrayList<String> ingredients;

    @Before
    public void setUp() throws Exception {
        accessToken = new UserClient().registerAndGetToken(UserGenerator.getRandom());
        ingredients = OrderGenerator.getIngredients();
    }

    @After
    public void tearDown() {
        new UserClient().delete(accessToken);
    }

    @Test
    @DisplayName("Отправить запрос на создание заказа для авторизованного пользователя")
    @Description("Заказ должен быть создан")
    public void createOrderForAuthorizedUserAndValidIngredients() {
        OrderListResponse userOrders = new OrderClient().getMappedOrdersPerUser(accessToken);
        int ordersCountBeforeRequest = userOrders.totalToday;

        Response response = new OrderClient().create(Map.of("ingredients", ingredients) , accessToken);
        StellarburgersGeneralResponse mappedCreateResponse = response.getBody().as(StellarburgersGeneralResponse.class);
        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(200));
        assertTrue(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);

        userOrders = new OrderClient().getMappedOrdersPerUser(accessToken);
        assertEquals(String.format(ErrorMessageConstants.WRONG_FIELD_VALUE, "totalToday"), ordersCountBeforeRequest + 1, userOrders.totalToday);
    }

    @Test
    @DisplayName("Отправить запрос на создание заказа без ингредиентов")
    @Description("Заказ не должен быть создан")
    public void createOrderForAuthorizedUserWithoutIngredients() {
        OrderListResponse userOrders = new OrderClient().getMappedOrdersPerUser(accessToken);
        int ordersCountBeforeRequest = userOrders.totalToday;

        Response response = new OrderClient().create(Map.of("ingredients", new ArrayList<>()) , accessToken);
        StellarburgersGeneralResponse mappedCreateResponse = response.getBody().as(StellarburgersGeneralResponse.class);

        assertFalse(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);
        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(400));
        assertThat(ErrorMessageConstants.MESSAGE_IS_DIFFERENT, mappedCreateResponse.message, equalTo("Ingredient ids must be provided"));

        userOrders = new OrderClient().getMappedOrdersPerUser(accessToken);
        assertEquals(String.format(ErrorMessageConstants.WRONG_FIELD_VALUE, "totalToday"), ordersCountBeforeRequest, userOrders.totalToday);
    }

    @Test
    @DisplayName("Отправить запрос на создание заказа, где в составе есть невалидный хеш ингредиента")
    @Description("Заказ не должен быть создан")
    public void createOrderForAuthorizedUserContainsInvalidIngredient() {
        OrderListResponse userOrders = new OrderClient().getMappedOrdersPerUser(accessToken);
        int ordersCountBeforeRequest = userOrders.totalToday;

        ingredients.add("invalidValue");
        Response response = new OrderClient().create(Map.of("ingredients", ingredients) , accessToken);
        StellarburgersGeneralResponse mappedCreateResponse = response.getBody().as(StellarburgersGeneralResponse.class);

        assertFalse(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);
        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(500));
        assertThat(ErrorMessageConstants.MESSAGE_IS_DIFFERENT, mappedCreateResponse.message, equalTo("One or more ids provided are incorrect"));

        userOrders = new OrderClient().getMappedOrdersPerUser(accessToken);
        assertEquals(String.format(ErrorMessageConstants.WRONG_FIELD_VALUE, "totalToday"), ordersCountBeforeRequest, userOrders.totalToday);
    }

    @Test
    @DisplayName("Отправить запрос на создание заказа только с невалидным хешем ингредиента")
    @Description("Заказ не должен быть создан")
    public void createOrderForAuthorizedUserWithInvalidIngredients() {
        OrderListResponse userOrders = new OrderClient().getMappedOrdersPerUser(accessToken);
        int ordersCountBeforeRequest = userOrders.totalToday;

        Response response = new OrderClient().create(Map.of("ingredients", new ArrayList<>(List.of("invalidValue"))) , accessToken);
        StellarburgersGeneralResponse mappedCreateResponse = response.getBody().as(StellarburgersGeneralResponse.class);

        assertFalse(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);
        assertThat(ErrorMessageConstants.MESSAGE_IS_DIFFERENT, mappedCreateResponse.message, equalTo("One or more ids provided are incorrect"));
        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(500));

        userOrders = new OrderClient().getMappedOrdersPerUser(accessToken);
        assertEquals(String.format(ErrorMessageConstants.WRONG_FIELD_VALUE, "totalToday"), ordersCountBeforeRequest, userOrders.totalToday);
    }

    @Test
    @DisplayName("Отправить запрос на создание заказа без авторизации")
    @Description("Заказ не должен быть создан")
    public void createOrderForNotAuthorizedUser()  {
        Response response = new OrderClient().create(Map.of("ingredients", ingredients) , "");
        StellarburgersGeneralResponse mappedCreateResponse = response.getBody().as(StellarburgersGeneralResponse.class);
        assertFalse(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);
        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(401));
    }
}
