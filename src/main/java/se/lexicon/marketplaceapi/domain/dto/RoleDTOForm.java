package se.lexicon.marketplaceapi.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class RoleDTOForm {
    @NotBlank(message = "Role name is required")
    private String name;
}
