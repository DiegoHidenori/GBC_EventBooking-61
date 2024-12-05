package ca.gbc.userservice.service;

import ca.gbc.userservice.dto.UserRequest;
import ca.gbc.userservice.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(String email, UserRequest userRequest);
    void deleteUser(String email);
    UserResponse getUser(String email);
    UserResponse getUserById(Long userId);
    boolean doesUserExist(Long userId);

}
