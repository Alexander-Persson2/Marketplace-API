package se.lexicon.marketplaceapi.service;

import se.lexicon.marketplaceapi.domain.dto.RoleDTOForm;
import se.lexicon.marketplaceapi.domain.dto.RoleDTOView;

import java.util.List;

public interface RoleService {
    List<RoleDTOView> getAllRoles();

    RoleDTOView createRole(RoleDTOForm roleDTOForm);
}
