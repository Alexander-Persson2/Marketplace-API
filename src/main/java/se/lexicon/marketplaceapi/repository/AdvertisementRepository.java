package se.lexicon.marketplaceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.marketplaceapi.domain.entity.Advertisement;
import se.lexicon.marketplaceapi.domain.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    List<Advertisement> findByExpiryDateAfter(LocalDateTime date);

    List<Advertisement> findByExpiryDateBefore(LocalDateTime date);

    List<Advertisement> findByUserEmail(String email);

    List<Advertisement> findByUser(User user);

    default List<Advertisement> findActiveAds() {
        return findByExpiryDateAfter(LocalDateTime.now());
    }
}
