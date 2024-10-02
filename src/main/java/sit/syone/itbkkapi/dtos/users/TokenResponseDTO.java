package sit.syone.itbkkapi.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenResponseDTO {
    private String access_token;
    private String refresh_token;
}

