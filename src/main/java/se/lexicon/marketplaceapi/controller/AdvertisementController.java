package se.lexicon.marketplaceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.marketplaceapi.domain.dto.AdvertisementDTOForm;
import se.lexicon.marketplaceapi.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi.service.AdvertisementService;

import java.util.List;

@RestController
@RequestMapping("/api/ads")
public class AdvertisementController {

    private final AdvertisementService advertisementService;

    public AdvertisementController(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }
    @Operation(summary = "Create a new advertisement", description = "Creates an advertisement with title, description, price, and expiry date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Advertisement successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping("/create")
    public ResponseEntity<AdvertisementDTOView> createAd(@Valid @RequestBody AdvertisementDTOForm adDTO) {
        AdvertisementDTOView adDTOView = advertisementService.createAd(adDTO);
        return new ResponseEntity<>(adDTOView, HttpStatus.CREATED);
    }
    @Operation(summary = "Get active advertisements", description = "Retrieves a list of all active (non-expired) advertisements.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active advertisements retrieved successfully")
    })
    @GetMapping("/active")
    public ResponseEntity<List<AdvertisementDTOView>> getActiveAds() {
        List<AdvertisementDTOView> ads = advertisementService.getActiveAds();
        return new ResponseEntity<>(ads, HttpStatus.OK);
    }
    @Operation(summary = "Get advertisements by user", description = "Retrieves all advertisements created by the specified user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's advertisements retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{email}")
    public ResponseEntity<List<AdvertisementDTOView>> getAdsByUser(@PathVariable String email) {
        List<AdvertisementDTOView> ads = advertisementService.getAdsByUser(email);
        return new ResponseEntity<>(ads, HttpStatus.OK);
    }
    @Operation(summary = "Delete advertisement by ID", description = "Deletes an advertisement by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Advertisement successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Advertisement not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        advertisementService.deleteAd(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
