package com.software.grey.services;


import com.software.grey.exceptions.UserExistsException;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.BasicUser;
import com.software.grey.models.entities.GoogleUser;
import com.software.grey.models.enums.Role;
import com.software.grey.models.enums.Tier;
import com.software.grey.models.mappers.UserMapper;
import com.software.grey.repositories.BasicUserRepo;
import com.software.grey.repositories.GoogleUserRepo;
import com.software.grey.repositories.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepo userRepo;
    private BasicUserRepo basicUserRepo;
    private GoogleUserRepo googleUserRepo;
    private UserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void save(UserDTO userDTO) {
        if(userExists(userDTO))
            throw new UserExistsException("User already exists");

        userDTO.password = bCryptPasswordEncoder.encode(userDTO.password);
        BasicUser user = BasicUser.builder()
                .role(Role.ROLE_USER)
                .tier(Tier.Standard)
                .registrationType("basic")
                .enabled(true)
                .build();

        user = userMapper.toUser(userDTO, user);
        basicUserRepo.save(user);
    }

    public void saveGoogleUser(OAuth2User principal) {
        UserDTO userDTO = new UserDTO();
        userDTO.username = principal.getAttribute("email");
        userDTO.username = userDTO.username.split("@")[0];
        userDTO.email = principal.getAttribute("email");
        userDTO.externalID = principal.getAttribute("sub");

        saveGoogleUser(userDTO);
    }

    public void saveGoogleUser(UserDTO userDTO) {
        if(userExists(userDTO)) {
            return;
        }

        GoogleUser user = GoogleUser.builder()
                .externalID(userDTO.externalID)
                .role(Role.ROLE_USER)
                .tier(Tier.Standard)
                .registrationType("google")
                .enabled(true)
                .build();

        user = userMapper.toGoogleUser(userDTO, user);
        googleUserRepo.save(user);
    }

    public boolean userExists(UserDTO userDTO) {
        return userRepo.existsByUsername(userDTO.username) || userRepo.existsByEmail(userDTO.email);
    }
}
