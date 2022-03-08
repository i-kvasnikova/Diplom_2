package praktikum.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StellarburgersGeneralResponse {

    public String message;
    public boolean success;
}
