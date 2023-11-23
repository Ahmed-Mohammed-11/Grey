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
import org.simplejavamail.api.email.Email;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepo userRepo;
    private UserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private EmailSender emailSender;

    public void save(UserDTO userDTO) {
        if(userExists(userDTO))
            throw new UserExistsException("User already exists");

        userDTO.password = bCryptPasswordEncoder.encode(userDTO.password);
        User user = User.builder()
                .role(Role.ROLE_USER)
                .tier(Tier.Standard)
                .enabled(true)
                .build();
        user = userMapper.toUser(userDTO, user);
        userRepo.save(user);
        EmailDetails emailDetails = EmailDetails.builder()
                        .from("omartammam25@gmail.com")
                        .to(userDTO.email)
                        .subject("Grey welcomes you :)")
                        .content("enta ragl gd3")
                        .build();

        emailSender.send(emailDetails);
    }

    public boolean userExists(UserDTO userDTO){
        return userRepo.existsByUsername(userDTO.username) || userRepo.existsByEmail(userDTO.email);
    }
}
