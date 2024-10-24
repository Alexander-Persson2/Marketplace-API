package se.lexicon.marketplaceapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.marketplaceapi.domain.entity.Advertisement;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    List<Advertisement> findByExpiryDateAfter(LocalDateTime date);

    List<Advertisement> findByExpiryDateBefore(LocalDateTime date);

    List<Advertisement> findByUserEmail(String email);
}
