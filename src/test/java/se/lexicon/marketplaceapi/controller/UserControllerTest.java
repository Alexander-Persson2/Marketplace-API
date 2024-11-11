package se.lexicon.marketplaceapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import se.lexicon.marketplaceapi.domain.dto.*;
import se.lexicon.marketplaceapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // Added import
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterUser() throws Exception {
        // Arrange
        Set<RoleDTOForm> roles = new HashSet<>();
        roles.add(RoleDTOForm.builder().name("ROLE_USER").build());

        UserDTOForm userDTOForm = UserDTOForm.builder()
                .email("test@example.com")
                .password("password123")
                .firstName("John")
                .lastName("Doe")
                .roles(roles)
                .build();

        Set<RoleDTOView> roleViews = new HashSet<>();
        roleViews.add(RoleDTOView.builder().id(1L).name("ROLE_USER").build());

        UserDTOView userDTOView = UserDTOView.builder()
                .email("test@example.com")
                .roles(roleViews)
                .build();

        when(userService.register(any(UserDTOForm.class))).thenReturn(userDTOView);

        // Act & Assert
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTOForm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.roles[0].name").value("ROLE_USER"));
    }

    @Test
    void testLogin() throws Exception {
        // Arrange
        LoginForm loginForm = LoginForm.builder()
                .email("test@example.com")
                .password("password123")
                .build();

        AdvertisementDTOView adDTOView = AdvertisementDTOView.builder()
                .id(1L)
                .title("Ad Title")
                .description("Ad Description")
                .price(99.99)
                .category("Electronics")
                .userEmail("test@example.com")
                .build();

        List<AdvertisementDTOView> ads = Collections.singletonList(adDTOView);

        when(userService.authenticateAndGetAds(loginForm.getEmail(), loginForm.getPassword())).thenReturn(ads);

        // Act & Assert
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginForm)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Ad Title"));
    }

    @Test
    void testGetUserByEmail() throws Exception {
        // Arrange
        String email = "user@example.com";
        Set<RoleDTOView> roles = new HashSet<>();
        roles.add(RoleDTOView.builder().id(1L).name("ROLE_USER").build());

        UserDTOView userDTOView = UserDTOView.builder()
                .email(email)
                .roles(roles)
                .build();

        when(userService.getByEmail(email)).thenReturn(userDTOView);

        // Act & Assert
        mockMvc.perform(get("/api/users/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.roles[0].name").value("ROLE_USER"));
    }

    @Test
    void testDisableUser() throws Exception {
        // Arrange
        String email = "user@example.com";
        doNothing().when(userService).disableByEmail(email);

        // Act & Assert
        mockMvc.perform(post("/api/users/{email}/disable", email))
                .andExpect(status().isNoContent());
    }

    @Test
    void testEnableUser() throws Exception {
        // Arrange
        String email = "user@example.com";
        doNothing().when(userService).enableByEmail(email);

        // Act & Assert
        mockMvc.perform(post("/api/users/{email}/enable", email))
                .andExpect(status().isNoContent());
    }
}