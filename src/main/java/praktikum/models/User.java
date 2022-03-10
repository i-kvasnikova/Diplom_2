package praktikum.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Setter
    private String email;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Setter
    private String password;

}
