package se.lexicon.marketplaceapi.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

public class AdvertisementDTOView {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime expiryDate;
    private String userEmail;
}
