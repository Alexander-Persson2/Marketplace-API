package se.lexicon.marketplaceapi.service;

import se.lexicon.marketplaceapi.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi.domain.dto.UserDTOView;

import java.util.List;

public interface UserService {
    UserDTOView register(UserDTOForm userDTOForm);

    UserDTOView getByEmail(String email);

    void disableByEmail(String email);

    void enableByEmail(String email);

    List<AdvertisementDTOView> authenticateAndGetAds(String email, String password);
}
