package com.software.grey.services;

import com.software.grey.exceptions.UserExistsException;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.BasicUser;
import com.software.grey.models.entities.GoogleUser;
import com.software.grey.models.entities.UserVerification;
import com.software.grey.models.enums.Role;
import com.software.grey.models.enums.Tier;
import com.software.grey.models.mappers.UserMapper;
import com.software.grey.repositories.BasicUserRepo;
import com.software.grey.repositories.GoogleUserRepo;
import com.software.grey.repositories.UserRepo;
import com.software.grey.repositories.UserVerificationRepo;
import com.software.grey.utils.SecurityUtils;
import com.software.grey.utils.emailsender.EmailSender;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepo userRepo;
    private BasicUserRepo basicUserRepo;
    private GoogleUserRepo googleUserRepo;
    private UserVerificationRepo userVerificationRepo;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailSender emailSender;
    private SecurityUtils securityUtils;
    @Value("${grey.from}") private String fromAddress;
    @Value("${back.url}") private String backendURL;

    public UserService(UserRepo userRepo, BasicUserRepo basicUserRepo, GoogleUserRepo googleUserRepo,
                       UserVerificationRepo userVerificationRepo, UserMapper userMapper,
                       BCryptPasswordEncoder bCryptPasswordEncoder, EmailSender emailSender,
                       SecurityUtils securityUtils) {
        this.userRepo = userRepo;
        this.basicUserRepo = basicUserRepo;
        this.googleUserRepo = googleUserRepo;
        this.userVerificationRepo = userVerificationRepo;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailSender = emailSender;
        this.securityUtils = securityUtils;
    }

    public void save(UserDTO userDTO) {
        if(userExists(userDTO))
            throw new UserExistsException("User already exists");

        userDTO.password = bCryptPasswordEncoder.encode(userDTO.password);
        BasicUser user = BasicUser.builder()
                .role(Role.ROLE_USER)
                .tier(Tier.Standard)
                .registrationType("basic")
                .enabled(false)
                .build();

        user = userMapper.toUser(userDTO, user);

        String confirmationCode = securityUtils.generateConfirmationCode();
        UserVerification userVerification = UserVerification.builder()
                .user(user)
                .registrationConfirmationCode(confirmationCode)
                .build();
        userVerificationRepo.save(userVerification);

        emailSender.send(userDTO, confirmationCode);
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
        userRepo.save(user);
    }

    @Transactional
    public void verifyUser(String userID, String confirmationCode){
        // get user id and register code from db and check if they match
        String currentUserConfirmationCode = getConfirmationCode(userID);
        if(currentUserConfirmationCode.equals(confirmationCode)){
            userRepo.setEnableById(userID);
        }else{
            throw new EntityNotFoundException("Invalid URL");
        }
    }

    private String getConfirmationCode(String userID){
        return userVerificationRepo.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userID))
                .getRegistrationConfirmationCode();
    }

    public boolean userExists(UserDTO userDTO) {
        return userRepo.existsByUsername(userDTO.username) || userRepo.existsByEmail(userDTO.email);
    }
}
