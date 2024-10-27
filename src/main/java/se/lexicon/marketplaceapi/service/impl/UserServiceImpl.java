package se.lexicon.marketplaceapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.marketplaceapi.domain.dto.RoleDTOView;
import se.lexicon.marketplaceapi.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi.domain.dto.UserDTOView;
import se.lexicon.marketplaceapi.domain.entity.Role;
import se.lexicon.marketplaceapi.domain.entity.User;
import se.lexicon.marketplaceapi.repository.RoleRepository;
import se.lexicon.marketplaceapi.repository.UserRepository;
import se.lexicon.marketplaceapi.service.UserService;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDTOView register(UserDTOForm userDTOForm) {
        if (userRepository.findByEmail(userDTOForm.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        Set<Role> roles = userDTOForm.getRoles().stream()
                .map(roleDTO -> roleRepository.findByName(roleDTO.getName())
                        .orElseThrow(() -> new IllegalArgumentException("Role not found")))
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
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

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
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setExpired(true);
        userRepository.save(user);
    }

    @Override
    public void enableByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setExpired(false);
        userRepository.save(user);
    }

}
