package se.lexicon.marketplaceapi.service;

import se.lexicon.marketplaceapi.domain.dto.AdvertisementDTOForm;
import se.lexicon.marketplaceapi.domain.dto.AdvertisementDTOView;

import java.util.List;

public interface AdvertisementService {
    AdvertisementDTOView createAd(AdvertisementDTOForm adDTO);

    List<AdvertisementDTOView> getActiveAds();

    List<AdvertisementDTOView> getAdsByUser(String userEmail);

    void deleteAd(Long id);
}
