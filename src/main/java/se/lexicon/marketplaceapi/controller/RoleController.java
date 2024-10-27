package se.lexicon.marketplaceapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.marketplaceapi.domain.dto.RoleDTOForm;
import se.lexicon.marketplaceapi.domain.dto.RoleDTOView;
import se.lexicon.marketplaceapi.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDTOView>> getAllRoles() {
        List<RoleDTOView> roles = roleService.getAllRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<RoleDTOView> createRole(@RequestBody RoleDTOForm roleDTOForm) {
        RoleDTOView newRole = roleService.createRole(roleDTOForm);
        return new ResponseEntity<>(newRole, HttpStatus.CREATED);
    }

}
