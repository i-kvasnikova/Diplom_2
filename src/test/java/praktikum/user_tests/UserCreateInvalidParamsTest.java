package praktikum.user_tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.clients.UserClient;
import praktikum.generators.UserGenerator;
import praktikum.models.User;
import praktikum.responses.UserRegisterData;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class UserCreateInvalidParamsTest {
    @Parameterized.Parameter
    public User user;

    @Parameterized.Parameters(name = "{index}: {0}")
    public static Object[] getUserData() {
        return new Object[][] {
                { UserGenerator.getWithoutName() },
                { UserGenerator.getWithoutEmail() },
                { UserGenerator.getWithoutPassword() }
        };
    }

    @Test
//    @DisplayName("Запрос на создание пользователя без обязательного параметра")
    @Description("Переданы не все обязательные параметры, возвращается сообщение об ошибке")
    public void invalidCreateRequestIsNotAllowed() {
        Response response = new UserClient().create(user);
        UserRegisterData mappedCreateResponse = response.getBody().as(UserRegisterData.class);
        assertThat(response.statusCode(), equalTo(403));
        assertFalse(mappedCreateResponse.success);
        assertThat(mappedCreateResponse.message, equalTo("Email, password and name are required fields"));
    }
}
