package se.lexicon.marketplaceapi.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import se.lexicon.marketplaceapi.domain.entity.Role;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindByName_ShouldReturnRole() {
        Optional<Role> role = roleRepository.findByName("USER");
        assertTrue(role.isPresent());
    }
}
