package praktikum.user_tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import praktikum.clients.UserClient;
import praktikum.configuration.ErrorMessageConstants;
import praktikum.generators.UserGenerator;
import praktikum.models.User;
import praktikum.responses.UserRegisterData;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Epic("Тестирование метода редактирования данных пользователя")
@RunWith(Parameterized.class)
public class UserEditParamsTest {
    @Parameterized.Parameter
    public User userToEdit;
    private static User userToCreate;
    private static UserClient userClient;
    private static UserRegisterData savedData;

    @After
    public void tearDown() {
        new UserClient().delete(savedData.accessToken);
    }

    @Parameterized.Parameters(name = "Редактирование данных о пользователе [{index}]: {0}")
    public static Object[] getUserData() {
        userToCreate = UserGenerator.getRandom();
        userClient = new UserClient();
        return new Object[][] {
                { UserGenerator.getRandomEmail(userToCreate) },
                { UserGenerator.getRandomName(userToCreate) },
                { UserGenerator.getRandomPassword(userToCreate) }
        };
    }

    @Test
    @Description("Пользователь авторизован")
    public void testEditUserAuthorizedValidData(){
        savedData = userClient.register(userToCreate);

        Response response = userClient.edit(userToEdit, savedData.accessToken);
        UserRegisterData mappedCreateResponse = response.getBody().as(UserRegisterData.class);

        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(200));
        assertTrue(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);
        assertThat(String.format(ErrorMessageConstants.FIELD_VALUE_IS_DIFFERENT, "name"), mappedCreateResponse.user.getName(), equalTo(userToEdit.getName()));
        assertThat(String.format(ErrorMessageConstants.FIELD_VALUE_IS_DIFFERENT, "email"), mappedCreateResponse.user.getEmail(), equalTo(userToEdit.getEmail()));

        userClient.logout(Map.of("token", savedData.refreshToken));
        response = userClient.login(userToEdit);
        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(200));
    }

    @Test
    @Description("Пользователь не авторизован")
    public void testEditUserNotAuthorizedValidData(){
        savedData = userClient.register(userToCreate);

        Response response = userClient.edit(userToEdit, "");
        UserRegisterData mappedCreateResponse = response.getBody().as(UserRegisterData.class);

        assertThat(ErrorMessageConstants.STATUS_CODE_IS_DIFFERENT, response.statusCode(), equalTo(401));
        assertFalse(ErrorMessageConstants.WRONG_SUCCESS_STATUS, mappedCreateResponse.success);
        assertThat(ErrorMessageConstants.MESSAGE_IS_DIFFERENT, mappedCreateResponse.message, equalTo("You should be authorised"));
    }
}
