package com.software.grey.services;

import com.software.grey.exceptions.UserExistsException;
import com.software.grey.models.enums.Role;
import com.software.grey.models.enums.Tier;
import com.software.grey.models.entities.User;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.mappers.UserMapper;
import com.software.grey.repositories.UserRepo;
import com.software.grey.utils.emailsender.EmailDetails;
import com.software.grey.utils.emailsender.EmailSender;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.simplejavamail.api.email.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailSender emailSender;
    @Value("${grey.from}") private String fromAddress;

    public UserService(UserRepo userRepo, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder, EmailSender emailSender) {
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailSender = emailSender;
    }

    public void save(UserDTO userDTO) {
        if(userExists(userDTO))
            throw new UserExistsException("User already exists");

        userDTO.password = bCryptPasswordEncoder.encode(userDTO.password);
        User user = User.builder()
                .role(Role.ROLE_USER)
                .tier(Tier.Standard)
                .enabled(false)
                .build();
        user = userMapper.toUser(userDTO, user);
        userRepo.save(user);
        sendVerificationEmail(userDTO.email);
    }


    private void sendVerificationEmail(String toAddress){
        EmailDetails emailDetails = EmailDetails.builder()
                .from(fromAddress)
                .to(toAddress)
                .subject("Welcome to Grey!")
                .content(populateEmailContentMessage())
                .build();

        emailSender.send(emailDetails);
    }

    // TODO add a link containing site url + verify URL + ?userID=&registerationCode=
    private String populateEmailContentMessage(){
        return "Please click the following link to verify email: ";
    }

    public void verifyUser(String userID, String registerCode){
        // get user id and register code from db and check if they match
        if(true){
            // update user's enabled field in db to true
        }else{
            // return user not found exception
        }
    }

    public boolean userExists(UserDTO userDTO){
        return userRepo.existsByUsername(userDTO.username) || userRepo.existsByEmail(userDTO.email);
    }
}
