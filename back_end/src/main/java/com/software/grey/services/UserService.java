package com.software.grey.services;

import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.dtos.UserResponseDTO;
import com.software.grey.models.entities.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

public interface UserService {

    UserResponseDTO getUser();

    void save(UserDTO userDTO);

    void saveGoogleUser(OAuth2User principal);

    void saveGoogleUser(UserDTO userDTO);

    void verifyUser(String userID, String confirmationCode);

    boolean userExists(UserDTO userDTO);

    User findByUserName(String userName);

    void updateUser(UserDTO userDTO);
}
