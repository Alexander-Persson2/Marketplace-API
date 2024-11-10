package se.lexicon.marketplaceapi.domain.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class AdvertisementDTOForm {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive number")
    private Double price;

    @NotNull(message = "Expiry date is required")
    @Future(message = "Expiry date must be in the future")
    private LocalDateTime expiryDate;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "User email is required")
    @Email(message = "Invalid email format")
    private String userEmail;

    @NotBlank(message = "Password is required")
    private String password;

}
