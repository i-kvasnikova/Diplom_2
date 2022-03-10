package praktikum.configuration;

public class ErrorMessageConstants {
    private ErrorMessageConstants() { throw new IllegalStateException("Utility class"); }

    public static final String WRONG_FIELD_VALUE = "Неверное значение поля '%s'.";
    public static final String FIELD_VALUE_IS_DIFFERENT = "Значение поля '%s' отличается от ожидаемого.";
    public static final String WRONG_SUCCESS_STATUS = String.format(WRONG_FIELD_VALUE, "success");
    public static final String STATUS_CODE_IS_DIFFERENT = String.format(FIELD_VALUE_IS_DIFFERENT, "statusCode");
    public static final String MESSAGE_IS_DIFFERENT = String.format(FIELD_VALUE_IS_DIFFERENT, "message");
}
