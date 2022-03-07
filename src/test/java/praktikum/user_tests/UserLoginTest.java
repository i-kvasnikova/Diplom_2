package praktikum.user_tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.clients.UserClient;
import praktikum.generators.UserGenerator;
import praktikum.models.User;
import praktikum.responses.UserRegisterData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

public class UserLoginTest {

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
    @DisplayName("Успешная авторизация под существующим пользователем")
    @Description("Переданы верный email и пароль")
    public void testLoginWithValidUser() {
        userData = userClient.register(userData.user);

        userClient.create(userData.user);
        User userToLogin = UserGenerator.getWithoutName(userData.user);
        Response response = userClient.login(userToLogin);
        UserRegisterData mappedCreateResponse = response.getBody().as(UserRegisterData.class);

        assertThat(response.statusCode(), equalTo(200));
        assertTrue(mappedCreateResponse.success);
        assertThat(mappedCreateResponse.user.getName(), equalTo(userData.user.getName()));
        assertThat(mappedCreateResponse.user.getEmail(), equalTo(userData.user.getEmail()));
        assertTrue(StringUtils.isNotBlank(mappedCreateResponse.accessToken));
        assertTrue(StringUtils.isNotBlank(mappedCreateResponse.refreshToken));
    }
}
