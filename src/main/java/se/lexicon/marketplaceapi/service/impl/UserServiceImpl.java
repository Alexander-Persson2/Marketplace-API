package se.lexicon.marketplaceapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.marketplaceapi.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi.domain.dto.RoleDTOView;
import se.lexicon.marketplaceapi.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi.domain.dto.UserDTOView;
import se.lexicon.marketplaceapi.domain.entity.Advertisement;
import se.lexicon.marketplaceapi.domain.entity.Role;
import se.lexicon.marketplaceapi.domain.entity.User;
import se.lexicon.marketplaceapi.exception.DataDuplicateException;
import se.lexicon.marketplaceapi.exception.DataNotFoundException;
import se.lexicon.marketplaceapi.repository.AdvertisementRepository;
import se.lexicon.marketplaceapi.repository.RoleRepository;
import se.lexicon.marketplaceapi.repository.UserRepository;
import se.lexicon.marketplaceapi.service.UserService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AdvertisementRepository advertisementRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, AdvertisementRepository advertisementRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.advertisementRepository = advertisementRepository;
    }

    @Override
    public UserDTOView register(UserDTOForm userDTOForm) {
        if (userRepository.findByEmail(userDTOForm.getEmail()).isPresent()) {
            throw new DataDuplicateException("Email already exists");
        }

        Set<Role> roles = userDTOForm.getRoles().stream()
                .map(roleDTO -> roleRepository.findByName(roleDTO.getName())
                        .orElseThrow(() -> new DataNotFoundException("Role not found")))
                .collect(Collectors.toSet());


        User user = User.builder()
                .email(userDTOForm.getEmail())
                .password(userDTOForm.getPassword())
                .firstName(userDTOForm.getFirstName())
                .lastName(userDTOForm.getLastName())
                .roles(roles)
                .build();

        userRepository.save(user);

        return UserDTOView.builder()
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> new RoleDTOView(role.getId(), role.getName()))
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public UserDTOView getByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        return UserDTOView.builder()
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> new RoleDTOView(role.getId(), role.getName()))
                        .collect(Collectors.toSet()))
                .build();
    }

    @Override
    public void disableByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        user.setExpired(true);
        userRepository.save(user);
    }

    @Override
    public void enableByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        user.setExpired(false);
        userRepository.save(user);
    }

    @Override
    public List<AdvertisementDTOView> authenticateAndGetAds(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new DataNotFoundException("Wrong password");
        }

        List<Advertisement> ads = advertisementRepository.findByUser(user);

        return ads.stream()
                .map(ad -> AdvertisementDTOView.builder()
                        .id(ad.getId())
                        .title(ad.getTitle())
                        .description(ad.getDescription())
                        .price(ad.getPrice())
                        .category(ad.getCategory())
                        .createdAt(ad.getCreatedAt())
                        .expiryDate(ad.getExpiryDate())
                        .userEmail(user.getEmail()).build()
                )
                .collect(Collectors.toList());
    }
}
