package ca.gbc.userservice.service;

import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;
import ca.gbc.userservice.model.User;
import ca.gbc.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        log.info("Processing user creation request: {}", userRequest);

        User user = User.builder()
                .userId(userRequest.userId())
                .name(userRequest.name())
                .email(userRequest.email())
                .role(userRequest.role())
                .userType(userRequest.userType())
                .build();

        userRepository.save(user);
        log.info("User created successfully with ID: {}", user.getUserId());

        return mapToUserResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        log.info("Retrieving all users");

        List<User> users = userRepository.findAll();
        log.info("Total users retrieved: {}", users.size());

        return users.stream().map(this::mapToUserResponse).toList();
    }

    private UserResponse mapToUserResponse(User user) {
        log.debug("Mapping user entity to response for user ID: {}", user.getUserId());

        return new UserResponse(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getUserType()
        );
    }

    @Override
    public UserResponse updateUser(String email, UserRequest userRequest) {
        log.info("Attempting to update user with email: {}", email);

        Optional<User> optionalUser = userRepository.findByEmail(email).stream().findFirst();
        if (optionalUser.isEmpty()) {
            log.warn("User with email {} not found", email);
            throw new IllegalArgumentException("User not found");
        }

        User user = optionalUser.get();
        user.setName(userRequest.name());
        user.setRole(userRequest.role());
        user.setUserType(userRequest.userType());

        User updatedUser = userRepository.save(user);
        log.info("User with email: {} updated successfully", updatedUser.getEmail());

        return mapToUserResponse(updatedUser);
    }

    @Override
    public void deleteUser(String email) {
        log.info("Attempting to delete user with email: {}", email);

        Optional<User> optionalUser = userRepository.findByEmail(email).stream().findFirst();
        if (optionalUser.isPresent()) {
            userRepository.delete(optionalUser.get());
            log.info("User with email: {} deleted successfully", email);
        } else {
            log.warn("User with email {} not found for deletion", email);
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public UserResponse getUser(String email) {
        log.info("Retrieving user with email: {}", email);

        Optional<User> optionalUser = userRepository.findByEmail(email).stream().findFirst();
        if (optionalUser.isPresent()) {
            log.info("User with email: {} found", email);
            return mapToUserResponse(optionalUser.get());
        } else {
            log.warn("User with email {} not found", email);
            throw new IllegalArgumentException("User not found");
        }
    }

}
