package com.software.grey.services;

import com.software.grey.models.entities.BasicUser;
import com.software.grey.models.entities.GoogleUser;
import com.software.grey.models.enums.Role;
import com.software.grey.models.enums.Tier;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.mappers.UserMapper;
import com.software.grey.repositories.GoogleUserRepo;
import com.software.grey.repositories.BasicUserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service

public class UserService {
    private BasicUserRepo userRepo;
    private GoogleUserRepo googleUserRepo;
    private UserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(BasicUserRepo userRepo, GoogleUserRepo googleUserRepo, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepo = userRepo;
        this.googleUserRepo = googleUserRepo;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void save(UserDTO userDTO) {
        userDTO.password = bCryptPasswordEncoder.encode(userDTO.password);
        BasicUser user = BasicUser.builder()
                .role(Role.ROLE_USER)
                .tier(Tier.Standard)
                .registrationType("basic")
                .enabled(true)
                .build();

        user = (BasicUser) userMapper.toUser(userDTO, user);
        userRepo.save(user);
    }

    public void saveGoogleUser(OAuth2User principal) {

        // TODO name can be not unique and needs change
        UserDTO userDTO = new UserDTO();
        userDTO.username = principal.getAttribute("name");
        userDTO.email = principal.getAttribute("email");

        if (!userExists(userDTO)) {
            return;
        }

        GoogleUser user = GoogleUser.builder()
                .externalID(principal.getAttribute("sub"))
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
