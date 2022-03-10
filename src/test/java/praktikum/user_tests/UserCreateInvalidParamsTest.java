package praktikum.user_tests;

import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.junit.Test;
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

@Epic("Тестирование метода создания пользователя с невалидными данными")
@RunWith(Parameterized.class)
public class UserCreateInvalidParamsTest {
    @Parameterized.Parameter
    public User user;
    private static UserClient userClient;

    @Parameterized.Parameters(name = "Создание пользователя без обязательного параметра [{index}]: {0}")
    public static Object[] getUserData() {
        userClient = new UserClient();
        return new Object[][] {
                { UserGenerator.getWithoutName() },
                { UserGenerator.getWithoutEmail() },
                { UserGenerator.getWithoutPassword() }
        };
    }

    @Test
    public void invalidCreateRequestIsNotAllowed() {
        Response response = userClient.create(user);
        UserRegisterData mappedCreateResponse = response.getBody().as(UserRegisterData.class);
        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(403));
        assertFalse(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);
        assertThat(ErrorMessageConstants.MESSAGE_IS_DIFFERENT, mappedCreateResponse.message, equalTo("Email, password and name are required fields"));
    }
}
