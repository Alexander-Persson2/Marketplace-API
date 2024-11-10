package se.lexicon.marketplaceapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "user")
@EqualsAndHashCode(exclude = "user")
@Builder

@Entity
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Double price;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiryDate;
    @Column(nullable = false)
    private String category;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
