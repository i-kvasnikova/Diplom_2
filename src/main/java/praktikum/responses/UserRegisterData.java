package praktikum.responses;

import lombok.NoArgsConstructor;
import lombok.Setter;
import praktikum.models.User;

@NoArgsConstructor
public class UserRegisterData extends StellarburgersGeneralResponse{
    public User user;
    @Setter public String accessToken;
    public String refreshToken;
}
