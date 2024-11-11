package se.lexicon.marketplaceapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import se.lexicon.marketplaceapi.domain.entity.Advertisement;
import se.lexicon.marketplaceapi.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class AdvertisementRepositoryTest {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        userRepository.save(user);

        Advertisement ad1 = new Advertisement();
        ad1.setTitle("Ad 1");
        ad1.setDescription("Description 1");
        ad1.setPrice(100.0);
        ad1.setCategory("Electronics");
        ad1.setCreatedAt(LocalDateTime.now().minusDays(1));
        ad1.setExpiryDate(LocalDateTime.now().plusDays(5));
        ad1.setUser(user);

        Advertisement ad2 = new Advertisement();
        ad2.setTitle("Ad 2");
        ad2.setDescription("Description 2");
        ad2.setPrice(200.0);
        ad2.setCategory("Books");
        ad2.setCreatedAt(LocalDateTime.now().minusDays(2));
        ad2.setExpiryDate(LocalDateTime.now().minusDays(1)); // Expired
        ad2.setUser(user);

        advertisementRepository.save(ad1);
        advertisementRepository.save(ad2);
    }

    @Test
    void findActiveAds_ShouldReturnNonExpiredAds() {
        // Act
        List<Advertisement> activeAds = advertisementRepository.findActiveAds();

        // Assert
        assertNotNull(activeAds);
        assertEquals(1, activeAds.size());
        assertEquals("Ad 1", activeAds.get(0).getTitle());
    }

    @Test
    void findByUserEmail_ShouldReturnUserAds() {
        // Act
        List<Advertisement> userAds = advertisementRepository.findByUserEmail("test@example.com");

        // Assert
        assertNotNull(userAds);
        assertEquals(2, userAds.size());
    }
}