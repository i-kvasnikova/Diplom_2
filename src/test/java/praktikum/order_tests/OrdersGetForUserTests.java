package praktikum.order_tests;

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
import praktikum.generators.UserGenerator;
import praktikum.responses.OrderListResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Epic("Тестирование метода получения заказа для пользователя")
public class OrdersGetForUserTests {

    String accessToken;

    @Before
    public void setUp() {
        accessToken = new UserClient().registerAndGetToken(UserGenerator.getRandom());
    }

    @After
    public void tearDown() {
        new UserClient().delete(accessToken);
    }

    @Test
    @DisplayName("Получить список заказов для авторизованного пользователя")
    @Description("Список успешно получен")
    public void testGetOrdersForAuthorizedUser(){
        Response response = new OrderClient().getAllPerUser(accessToken);
        OrderListResponse mappedCreateResponse = response.getBody().as(OrderListResponse.class);

        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(200));
        assertTrue(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);
    }

    @Test
    @DisplayName("Получить список заказов для неавторизованного пользователя")
    @Description("Список не получен, возвращается сообщение 'You should be authorised'")
    public void testGetOrdersForNotAuthorizedUser(){
        Response response = new OrderClient().getAllPerUser("");
        OrderListResponse mappedCreateResponse = response.getBody().as(OrderListResponse.class);

        assertFalse(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);
        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(401));
        assertThat(ErrorMessageConstants.MESSAGE_IS_DIFFERENT, mappedCreateResponse.message, equalTo("You should be authorised"));
    }
}
