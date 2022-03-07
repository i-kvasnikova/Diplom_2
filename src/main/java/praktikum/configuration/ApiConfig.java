package praktikum.configuration;

import org.aeonbits.owner.Config;

@Config.Sources({"file:src/main/resources/ApiConfig.properties"})
public interface ApiConfig extends Config {

    @Key("stellarburgers.url.base")
    String getBaseUrl();

    @Key("api.user.register")
    String getUserRegisterPath();

    @Key("api.user.login")
    String getUserLoginPath();

    @Key("api.user.logout")
    String getUserLogoutPath();

    @Key("api.user")
    String getUserDataPath();
}
