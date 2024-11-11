package se.lexicon.marketplaceapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import se.lexicon.marketplaceapi.domain.dto.AdvertisementDTOForm;
import se.lexicon.marketplaceapi.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi.service.AdvertisementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc; // Added import
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdvertisementController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters
public class AdvertisementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdvertisementService advertisementService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateAd() throws Exception {
        // Arrange
        AdvertisementDTOForm adDTOForm = AdvertisementDTOForm.builder()
                .title("Test Ad")
                .description("Test Description")
                .price(100.0)
                .expiryDate(LocalDateTime.now().plusDays(5))
                .category("Electronics")
                .userEmail("test@example.com")
                .password("password")
                .build();

        AdvertisementDTOView adDTOView = AdvertisementDTOView.builder()
                .id(1L)
                .title("Test Ad")
                .description("Test Description")
                .price(100.0)
                .category("Electronics")
                .createdAt(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusDays(5))
                .userEmail("test@example.com")
                .build();

        when(advertisementService.createAd(any(AdvertisementDTOForm.class))).thenReturn(adDTOView);

        // Act & Assert
        mockMvc.perform(post("/api/ads/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adDTOForm)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Ad"))
                .andExpect(jsonPath("$.price").value(100.0))
                .andExpect(jsonPath("$.userEmail").value("test@example.com"));
    }

    @Test
    void testGetActiveAds() throws Exception {
        // Arrange
        AdvertisementDTOView adDTOView = AdvertisementDTOView.builder()
                .id(1L)
                .title("Active Ad")
                .description("Active Description")
                .price(150.0)
                .category("Books")
                .createdAt(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusDays(10))
                .userEmail("active@example.com")
                .build();

        List<AdvertisementDTOView> ads = Collections.singletonList(adDTOView);

        when(advertisementService.getActiveAds()).thenReturn(ads);

        // Act & Assert
        mockMvc.perform(get("/api/ads/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Active Ad"));
    }

    @Test
    void testGetAdsByUser() throws Exception {
        // Arrange
        String email = "user@example.com";
        AdvertisementDTOView adDTOView = AdvertisementDTOView.builder()
                .id(1L)
                .title("User's Ad")
                .description("User's Description")
                .price(200.0)
                .category("Furniture")
                .createdAt(LocalDateTime.now())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .userEmail(email)
                .build();

        List<AdvertisementDTOView> ads = Collections.singletonList(adDTOView);

        when(advertisementService.getAdsByUser(email)).thenReturn(ads);

        // Act & Assert
        mockMvc.perform(get("/api/ads/user/{email}", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userEmail").value(email));
    }

    @Test
    void testDeleteAd() throws Exception {
        // Arrange
        Long adId = 1L;
        Mockito.doNothing().when(advertisementService).deleteAd(adId);

        // Act & Assert
        mockMvc.perform(delete("/api/ads/{id}", adId))
                .andExpect(status().isNoContent());
    }
}