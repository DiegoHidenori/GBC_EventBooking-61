package ca.gbc.userservice.controller;

import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        UserResponse createdUser = userService.createUser(userRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/user/" + createdUser.userId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdUser);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{email}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable("email") String email,
                                                   @RequestBody UserRequest userRequest) {
        UserResponse updatedUser = userService.updateUser(email, userRequest);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("email") String email) {
        UserResponse user = userService.getUser(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("userId") Long userId) {
        UserResponse user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}/exists")
    public ResponseEntity<Boolean> userExists(@PathVariable Long userId) {
        boolean exists = userService.doesUserExist(userId);
        return ResponseEntity.ok(exists);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable("email") String email) {
        userService.deleteUser(email);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("User deleted successfully");
    }

}
