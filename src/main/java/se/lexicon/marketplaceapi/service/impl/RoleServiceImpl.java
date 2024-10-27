package se.lexicon.marketplaceapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.lexicon.marketplaceapi.domain.dto.RoleDTOForm;
import se.lexicon.marketplaceapi.domain.dto.RoleDTOView;
import se.lexicon.marketplaceapi.domain.entity.Role;
import se.lexicon.marketplaceapi.repository.RoleRepository;
import se.lexicon.marketplaceapi.service.RoleService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<RoleDTOView> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(role -> RoleDTOView.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTOView createRole(RoleDTOForm roleDTOForm) {
        Role role = Role.builder()
                .name(roleDTOForm.getName())
                .build();

        Role savedRole = roleRepository.save(role);

        return RoleDTOView.builder()
                .id(savedRole.getId())
                .name(savedRole.getName())
                .build();
    }
}