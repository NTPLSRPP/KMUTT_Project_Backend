package sit.syone.itbkkapi.dtos.board;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisibilityDTO {
    @NotNull(message = "visibility is required")
    private String visibility;
}
