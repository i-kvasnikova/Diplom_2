package praktikum.user_tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.clients.UserClient;
import praktikum.configuration.ErrorMessageConstants;
import praktikum.generators.UserGenerator;
import praktikum.models.User;
import praktikum.responses.UserRegisterData;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Epic("Тестирование метода создания пользователя с уникальными и неуникальными данными")
public class UserCreateTest {

    private UserRegisterData userData;
    private UserClient userClient;

    @Before
    public void setUp() {
        userData = new UserRegisterData();
        userData.user = UserGenerator.getRandom();
        userClient = new UserClient();
    }

     @After
     public void tearDown() {
         userClient.delete(userData.accessToken);
    }

    @Test
    @DisplayName("Успешное создание уникальной учетной записи пользователя со всеми параметрами")
    @Description("Переданы все параметры пользователя, успешный запрос возвращает также accessToken и refreshToken")
    public void testCreateUniqueValidUser() {
        Response response = userClient.create(userData.user);
        UserRegisterData mappedCreateResponse = response.getBody().as(UserRegisterData.class);

        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(200));
        assertTrue(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);
        assertThat(String.format(ErrorMessageConstants.FIELD_VALUE_IS_DIFFERENT, "name"), mappedCreateResponse.user.getName(), equalTo(userData.user.getName()));
        assertThat(String.format(ErrorMessageConstants.FIELD_VALUE_IS_DIFFERENT, "email"), mappedCreateResponse.user.getEmail(), equalTo(userData.user.getEmail()));
        assertTrue(StringUtils.isNotBlank(mappedCreateResponse.accessToken));
        assertTrue(StringUtils.isNotBlank(mappedCreateResponse.refreshToken));
        userData.setAccessToken(mappedCreateResponse.accessToken.substring(7));
    }

    @Test
    @DisplayName("Создание неуникальной учетной записи пользователя со всеми параметрами")
    @Description("Нельзя создать двух одинаковых пользователей, запрос возвращает код 403 и сообщение 'User already exists'")
    public void testCreateNotUniqueValidUser() {
        userData = userClient.register(userData.user);

        Response response = userClient.create(userData.user);
        UserRegisterData mappedCreateResponse = response.getBody().as(UserRegisterData.class);

        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(403));
        assertFalse(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);
        assertThat(ErrorMessageConstants.MESSAGE_IS_DIFFERENT, mappedCreateResponse.message, equalTo("User already exists"));
    }

    @Test
    @DisplayName("Редактирование пользователя - передаем почту, которая уже используется")
    @Description("Нельзя создать двух одинаковых пользователей, запрос возвращает код 403 и сообщение 'User already exists'")
    public void testEditNotUniqueValidUser() {
        userData = userClient.register(userData.user);
        User otherExistingUser = UserGenerator.getRandom();
        String tokenForOtherUser = userClient.registerAndGetToken(otherExistingUser);
        userData.user.setEmail(otherExistingUser.getEmail());

        Response response = userClient.edit(userData.user, userData.accessToken);
        UserRegisterData mappedCreateResponse = response.getBody().as(UserRegisterData.class);

        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(403));
        assertFalse(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);
        assertThat(ErrorMessageConstants.MESSAGE_IS_DIFFERENT, mappedCreateResponse.message, equalTo("User with such email already exists"));

        userClient.delete(tokenForOtherUser);
    }
}