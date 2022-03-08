package praktikum.user_tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.clients.UserClient;
import praktikum.configuration.ErrorMessageConstants;
import praktikum.generators.UserGenerator;
import praktikum.models.User;
import praktikum.responses.UserRegisterData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

@Epic("Тестирование метода авторизации пользователя с невалидными данными")
@RunWith(Parameterized.class)
public class UserLoginInvalidParamsTest {
    @Parameterized.Parameter
    public User userToLogin;
    private static String accessToken;

    @AfterClass
    public static void tearDown() {
        new UserClient().delete(accessToken);
    }

    @Parameterized.Parameters(name = "Неуспешная авторизация [{index}]: {0}")
    public static Object[] getUserData() {
        User userToCreate = UserGenerator.getRandom();
        accessToken = new UserClient().create(userToCreate).getBody().as(UserRegisterData.class).accessToken.substring(7);
        return new Object[][] {
                { UserGenerator.getWithoutEmail(userToCreate) },
                { UserGenerator.getWithoutPassword(userToCreate) },
                { User.builder().email("wrong@value.ru").password(userToCreate.getPassword()).build() },
                { User.builder().email(userToCreate.getEmail()).password("wrongvalue").build() }
        };
    }

    @Test
    @Description("Параметры заполнены неправильно или отсутствуют")
    public void testLoginWithInvalidParams() {
        Response response = new UserClient().login(userToLogin);
        UserRegisterData mappedCreateResponse = response.getBody().as(UserRegisterData.class);

        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(401));
        assertFalse(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);
        assertThat(ErrorMessageConstants.MESSAGE_IS_DIFFERENT, mappedCreateResponse.message, equalTo("email or password are incorrect"));
    }
}
