package se.lexicon.marketplaceapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.marketplaceapi.domain.dto.AdvertisementDTOView;
import se.lexicon.marketplaceapi.domain.dto.LoginForm;
import se.lexicon.marketplaceapi.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi.domain.dto.UserDTOView;
import se.lexicon.marketplaceapi.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user", description = "Registers a user with an email, password, first name, last name, and roles.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User successfully created"),
            @ApiResponse(responseCode = "400", description = "Email already exists or invalid data")
    })
    @PostMapping("/register")
    public ResponseEntity<UserDTOView> registerUser(@RequestBody UserDTOForm userDTOForm) {
        UserDTOView userDTOView = userService.register(userDTOForm);
        return new ResponseEntity<>(userDTOView, HttpStatus.CREATED);
    }

    @Operation(summary = "Login user", description = "Authenticate a user and return their advertisements.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, advertisements retrieved"),
            @ApiResponse(responseCode = "401", description = "Unauthorized, invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<List<AdvertisementDTOView>> login(@Valid @RequestBody LoginForm loginForm) {
        List<AdvertisementDTOView> ads = userService.authenticateAndGetAds(loginForm.getEmail(), loginForm.getPassword());
        return new ResponseEntity<>(ads, HttpStatus.OK);
    }
    @Operation(summary = "Get user by email", description = "Retrieve a user's information by their email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{email}")
    public ResponseEntity<UserDTOView> getUserByEmail(@PathVariable String email) {
        UserDTOView userDTOView = userService.getByEmail(email);
        return new ResponseEntity<>(userDTOView, HttpStatus.OK);
    }
    @Operation(summary = "Disable a user account", description = "Sets the user's expired status to true.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User disabled successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{email}/disable")
    public ResponseEntity<Void> disableUser(@PathVariable String email) {
        userService.disableByEmail(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Enable a user account", description = "Sets the user's expired status to false.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User enabled successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{email}/enable")
    public ResponseEntity<Void> enableUser(@PathVariable String email) {
        userService.enableByEmail(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
