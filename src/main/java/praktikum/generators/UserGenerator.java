package praktikum.generators;

import com.github.javafaker.Faker;
import praktikum.models.User;

import java.util.Locale;

public class UserGenerator {
    private static final Faker faker = new Faker(new Locale("ru"));

    private UserGenerator() { throw new IllegalStateException("Utility class"); }

    public static User getRandom() {
        return User.builder()
                .email(faker.internet().emailAddress())
                .name(faker.lordOfTheRings().character().toLowerCase())
                .password(faker.internet().password())
                .build();
    }

    public static User getWithoutEmail() {
        return User.builder()
                .name(faker.lordOfTheRings().character().toLowerCase())
                .password(faker.internet().password())
                .build();
    }

    public static User getWithoutEmail(User user) {
        return User.builder()
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }

    public static User getWithoutName() {
        return User.builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .build();
    }

    public static User getWithoutName(User user) {
        return User.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    public static User getWithoutPassword() {
        return User.builder()
                .email(faker.internet().emailAddress())
                .name(faker.lordOfTheRings().character().toLowerCase())
                .build();
    }

    public static User getWithoutPassword(User user) {
        return User.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }

    public static User getRandomEmail(User user) {
        return User.builder()
                .email(faker.internet().emailAddress())
                .name(user.getName())
                .password(user.getPassword())
                .build();
    }

    public static User getRandomName(User user) {
        return User.builder()
                .email(user.getEmail())
                .name(faker.buffy().characters().toLowerCase())
                .password(user.getPassword())
                .build();
    }

    public static User getRandomPassword(User user) {
        return User.builder()
                .email(user.getEmail())
                .name(user.getName())
                .password(faker.internet().password())
                .build();
    }
}
