package se.lexicon.marketplaceapi.controller;

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

    @PostMapping("/create")
    public ResponseEntity<AdvertisementDTOView> createAd(@RequestBody AdvertisementDTOForm adDTO) {
        AdvertisementDTOView adDTOView = advertisementService.createAd(adDTO);
        return new ResponseEntity<>(adDTOView, HttpStatus.CREATED);
    }

    @GetMapping("/active")
    public ResponseEntity<List<AdvertisementDTOView>> getActiveAds() {
        List<AdvertisementDTOView> ads = advertisementService.getActiveAds();
        return new ResponseEntity<>(ads, HttpStatus.OK);
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<AdvertisementDTOView>> getAdsByUser(@PathVariable String email) {
        List<AdvertisementDTOView> ads = advertisementService.getAdsByUser(email);
        return new ResponseEntity<>(ads, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        advertisementService.deleteAd(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
