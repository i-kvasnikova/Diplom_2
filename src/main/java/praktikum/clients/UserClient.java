package praktikum.clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import praktikum.models.User;
import praktikum.responses.UserRegisterData;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserClient extends StellarBurgersRestClient {
    @Step("Создать пользователя")
    public Response create(User user) {
        return given()
                .spec(getBaseSpecification())
                .body(user)
                .log().all()
                .when()
                .post(apiConfig.getUserRegisterPath())
                .then()
                .extract().response();
    }

    @Step("Регистрация пользователя и сохранение токена")
    public UserRegisterData register(User user) {
        UserRegisterData registeredUser = create(user).getBody().as(UserRegisterData.class);
        registeredUser.user.setPassword(user.getPassword());
        registeredUser.setAccessToken(registeredUser.accessToken.substring(7));
        return registeredUser;
    }

    @Step("Регистрация пользователя и сохранение токена")
    public String registerAndGetToken(User user) {
        UserRegisterData registeredUser = create(user).getBody().as(UserRegisterData.class);
        return registeredUser.accessToken.substring(7);
    }

    @Step("Отправить запрос авторизации")
    public Response login(User user) {
        return given()
                .spec(getBaseSpecification())
                .body(user)
                .log().all()
                .when()
                .post(apiConfig.getUserLoginPath())
                .then()
                .extract().response();
    }

    @Step("Выйти из системы")
    public void logout(Map<String, String> inputDataMap) {
        given()
                .spec(getBaseSpecification())
                .body(inputDataMap)
                .log().all()
                .when()
                .post(apiConfig.getUserLogoutPath())
                .then()
                .statusCode(200);
    }

    @Step("Удалить пользователя")
    public void delete(String userToken) {
        if (userToken == null) {
            return;
        }
        given()
                .spec(getBaseSpecification())
                .auth().oauth2(userToken)
                .log().all()
                .when()
                .delete(apiConfig.getUserDataPath())
                .then()
                .statusCode(202);
    }

    @Step("Отредактировать пользователя: '{user}'")
    public Response edit(User user, String userToken) {
        return given()
                .spec(getBaseSpecification())
                .auth().oauth2(userToken)
                .body(user)
                .log().all()
                .when()
                .patch(apiConfig.getUserDataPath())
                .then()
                .extract().response();
    }
}
