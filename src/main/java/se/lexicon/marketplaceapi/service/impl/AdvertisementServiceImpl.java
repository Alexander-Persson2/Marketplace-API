package se.lexicon.marketplaceapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.marketplaceapi.domain.dto.AdvertisementDTOForm;
import se.lexicon.marketplaceapi.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi.domain.entity.Advertisement;
import se.lexicon.marketplaceapi.domain.entity.User;
import se.lexicon.marketplaceapi.repository.AdvertisementRepository;
import se.lexicon.marketplaceapi.repository.UserRepository;
import se.lexicon.marketplaceapi.service.AdvertisementService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdvertisementServiceImpl implements AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdvertisementServiceImpl(AdvertisementRepository advertisementRepository, UserRepository userRepository) {
        this.advertisementRepository = advertisementRepository;
        this.userRepository = userRepository;
    }

    @Override
    public AdvertisementDTOView createAd(AdvertisementDTOForm adDTO) {

        User user = userRepository.findByEmail(adDTO.getUserEmail())
                .orElseGet(() -> {

                    User newUser = new User();
                    newUser.setEmail(adDTO.getUserEmail());
                    newUser.setPassword(adDTO.getPassword());
                    newUser.setFirstName("Placeholder");
                    newUser.setLastName("User");
                    return userRepository.save(newUser);
                });


        Advertisement ad = Advertisement.builder()
                .title(adDTO.getTitle())
                .description(adDTO.getDescription())
                .price(adDTO.getPrice())
                .createdAt(LocalDateTime.now())
                .expiryDate(adDTO.getExpiryDate())
                .category(adDTO.getCategory())
                .user(user)
                .build();

        Advertisement savedAd = advertisementRepository.save(ad);

        return AdvertisementDTOView.builder()
                .id(savedAd.getId())
                .title(savedAd.getTitle())
                .description(savedAd.getDescription())
                .price(savedAd.getPrice())
                .createdAt(savedAd.getCreatedAt())
                .expiryDate(savedAd.getExpiryDate())
                .category(savedAd.getCategory())
                .userEmail(savedAd.getUser().getEmail())
                .build();
    }

    @Override
    public List<AdvertisementDTOView> getActiveAds() {
        return advertisementRepository.findByExpiryDateAfter(LocalDateTime.now()).stream()
                .map(ad -> AdvertisementDTOView.builder()
                        .id(ad.getId())
                        .title(ad.getTitle())
                        .description(ad.getDescription())
                        .price(ad.getPrice())
                        .createdAt(ad.getCreatedAt())
                        .expiryDate(ad.getExpiryDate())
                        .category(ad.getCategory())
                        .userEmail(ad.getUser().getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<AdvertisementDTOView> getAdsByUser(String userEmail) {
        return advertisementRepository.findByUserEmail(userEmail).stream()
                .map(ad -> AdvertisementDTOView.builder()
                        .id(ad.getId())
                        .title(ad.getTitle())
                        .description(ad.getDescription())
                        .price(ad.getPrice())
                        .createdAt(ad.getCreatedAt())
                        .expiryDate(ad.getExpiryDate())
                        .category(ad.getCategory())
                        .userEmail(ad.getUser().getEmail())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAd(Long id) {
        advertisementRepository.deleteById(id);
    }
}
