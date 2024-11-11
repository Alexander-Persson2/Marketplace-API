package se.lexicon.marketplaceapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import se.lexicon.marketplaceapi.domain.dto.RoleDTOForm;
import se.lexicon.marketplaceapi.domain.dto.RoleDTOView;
import se.lexicon.marketplaceapi.service.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // Added import
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllRoles() throws Exception {
        // Arrange
        RoleDTOView roleDTOView = RoleDTOView.builder()
                .id(1L)
                .name("ROLE_USER")
                .build();

        List<RoleDTOView> roles = Collections.singletonList(roleDTOView);

        when(roleService.getAllRoles()).thenReturn(roles);

        // Act & Assert
        mockMvc.perform(get("/api/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("ROLE_USER"));
    }

    @Test
    void testCreateRole() throws Exception {
        // Arrange
        RoleDTOForm roleDTOForm = RoleDTOForm.builder()
                .name("ROLE_ADMIN")
                .build();

        RoleDTOView roleDTOView = RoleDTOView.builder()
                .id(2L)
                .name("ROLE_ADMIN")
                .build();

        when(roleService.createRole(any(RoleDTOForm.class))).thenReturn(roleDTOView);

        // Act & Assert
        mockMvc.perform(post("/api/roles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleDTOForm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("ROLE_ADMIN"));
    }
}