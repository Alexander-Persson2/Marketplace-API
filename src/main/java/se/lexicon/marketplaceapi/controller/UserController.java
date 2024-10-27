package se.lexicon.marketplaceapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.lexicon.marketplaceapi.domain.dto.UserDTOForm;
import se.lexicon.marketplaceapi.domain.dto.UserDTOView;
import se.lexicon.marketplaceapi.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTOView> registerUser(@RequestBody UserDTOForm userDTOForm) {
        UserDTOView userDTOView = userService.register(userDTOForm);
        return new ResponseEntity<>(userDTOView, HttpStatus.CREATED);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTOView> getUserByEmail(@PathVariable String email) {
        UserDTOView userDTOView = userService.getByEmail(email);
        return new ResponseEntity<>(userDTOView, HttpStatus.OK);
    }

    @PostMapping("/{email}/disable")
    public ResponseEntity<Void> disableUser(@PathVariable String email) {
        userService.disableByEmail(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{email}/enable")
    public ResponseEntity<Void> enableUser(@PathVariable String email) {
        userService.enableByEmail(email);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
