package com.software.grey.services;

import com.software.grey.exceptions.UserExistsException;
import com.software.grey.exceptions.exceptions.FailedToUpdateException;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.entities.BasicUser;
import com.software.grey.models.entities.GoogleUser;
import com.software.grey.models.entities.User;
import com.software.grey.models.entities.UserVerification;
import com.software.grey.models.enums.Role;
import com.software.grey.models.enums.Tier;
import com.software.grey.models.mappers.UserMapper;
import com.software.grey.repositories.BasicUserRepo;
import com.software.grey.repositories.GoogleUserRepo;
import com.software.grey.repositories.UserRepo;
import com.software.grey.repositories.UserVerificationRepo;
import com.software.grey.utils.ErrorMessages;
import com.software.grey.utils.RegularExpressions;
import com.software.grey.utils.SecurityUtils;
import com.software.grey.utils.emailsender.EmailSender;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
//@AllArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final BasicUserRepo basicUserRepo;
    private final GoogleUserRepo googleUserRepo;
    private final UserVerificationRepo userVerificationRepo;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailSender emailSender;
    private final SecurityUtils securityUtils;
    private final boolean ENABLEMAIL = false;

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
        if (userExists(userDTO))
            throw new UserExistsException("User already exists");

        userDTO.password = bCryptPasswordEncoder.encode(userDTO.password);
        BasicUser user = BasicUser.builder()
                .role(Role.ROLE_USER)
                .tier(Tier.STANDARD)
                .registrationType("basic")
                .enabled(true)
                .build();

        user = userMapper.toUser(userDTO, user);

        String confirmationCode = securityUtils.generateConfirmationCode();
        UserVerification userVerification = UserVerification.builder()
                .user(user)
                .registrationConfirmationCode(confirmationCode)
                .build();
        userVerificationRepo.save(userVerification);

        if(ENABLEMAIL)
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
        if (userExists(userDTO)) {
            return;
        }

        GoogleUser user = GoogleUser.builder()
                .externalID(userDTO.externalID)
                .role(Role.ROLE_USER)
                .tier(Tier.STANDARD)
                .registrationType("google")
                .enabled(true)
                .build();

        user = userMapper.toGoogleUser(userDTO, user);
        googleUserRepo.save(user);
        userRepo.save(user);
    }

    @Transactional
    public void verifyUser(String userID, String confirmationCode) {
        // get user id and register code from db and check if they match
        String currentUserConfirmationCode = getConfirmationCode(userID);
        if (currentUserConfirmationCode.equals(confirmationCode)) {
            userRepo.setEnableById(userID);
        } else {
            throw new EntityNotFoundException("Invalid URL");
        }
    }

    private String getConfirmationCode(String userID) {
        return userVerificationRepo.findById(userID)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userID))
                .getRegistrationConfirmationCode();
    }

    public boolean userExists(UserDTO userDTO) {
        return userRepo.existsByUsername(userDTO.username) || userRepo.existsByEmail(userDTO.email);
    }

    public User findByUserName(String userName){
        return userRepo.findByUsername(userName);
    }

    /*
     * @return true if user was updated successfully, false otherwise
     */
    public void updateUser(UserDTO userDTO) {
        User user = securityUtils.getCurrentUser();
        if (user == null || userDTO == null || isNotValidDTO(userDTO)) {
            throw new FailedToUpdateException(ErrorMessages.INVALID_REQUEST_BODY);
        }

        if (user.getRegistrationType().equals("google")) {
            updateGoogleUser(userDTO, user);
        } else {
            updateBasicUser(userDTO, user);
        }
    }

    private void updateBasicUser(UserDTO userDTO, User user) {
        // if username has changed, check if it is valid and not already taken
        if (!user.getUsername().equals(userDTO.getUsername())) {
            isNotValidUserNameUpdate(userDTO);
        }

        // if email has changed, check if it is valid and not already taken
        if (!user.getEmail().equals(userDTO.getEmail())) {
            isNotValidEmailUpdate(userDTO);
        }

        BasicUser updatedUser = basicUserRepo.findByUsername(user.getUsername());
        String oldPassword = updatedUser.getPassword();
        updatedUser = userMapper.toUser(userDTO, updatedUser);

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().equals(oldPassword)) {   // if password has changed
            if (updatedUser.getPassword().matches(RegularExpressions.PASSWORD_REGEX)){  // if password is valid
                updatedUser.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword())); // encode password
            } else {
                throw new FailedToUpdateException(ErrorMessages.INVALID_PASSWORD);
            }
        }
        basicUserRepo.save(updatedUser);
        userRepo.save(updatedUser);
    }

    private void updateGoogleUser(UserDTO userDTO, User user) {
        // if username has changed, check if it is valid and not already taken
        if (!user.getUsername().equals(userDTO.getUsername())) {
            isNotValidUserNameUpdate(userDTO);
        }

        // if email has changed, check if it is valid and not already taken
        if (!user.getEmail().equals(userDTO.getEmail())) {
            throw new FailedToUpdateException("Cannot change email");
        }

        if (userDTO.getPassword() != null) {
            throw new FailedToUpdateException("Cannot change password");
        }

        GoogleUser updatedUser = googleUserRepo.findByUsername(user.getUsername());
        updatedUser = userMapper.toGoogleUser(userDTO, updatedUser);
        googleUserRepo.save(updatedUser);
        userRepo.save(updatedUser);
    }

    private void isNotValidUserNameUpdate(UserDTO userDTO) {
        if (Boolean.TRUE.equals(userRepo.existsByUsername(userDTO.getUsername()))) {
            throw new FailedToUpdateException(ErrorMessages.USERNAME_EXISTS);
        }

        if (!userDTO.getUsername().matches(RegularExpressions.USERNAME_REGEX)) {
            throw new FailedToUpdateException(ErrorMessages.INVALID_USERNAME);
        }
    }

    private void isNotValidEmailUpdate(UserDTO userDTO) {
        if (Boolean.TRUE.equals(userRepo.existsByEmail(userDTO.getEmail()))) {
            throw new FailedToUpdateException(ErrorMessages.EMAIL_EXISTS);
        }

        if (!userDTO.getEmail().matches(RegularExpressions.EMAIL_REGEX)) {
            throw new FailedToUpdateException(ErrorMessages.INVALID_EMAIL);
        }
    }

    private boolean isNotValidDTO(UserDTO userDTO) {
        return userDTO.username == null || userDTO.getEmail() == null;
    }
}
