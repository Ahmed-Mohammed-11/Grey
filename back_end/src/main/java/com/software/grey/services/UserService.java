package com.software.grey.services;

import com.software.grey.models.enums.Role;
import com.software.grey.models.enums.Tier;
import com.software.grey.models.entities.User;
import com.software.grey.models.dtos.UserDTO;
import com.software.grey.models.mappers.UserMapper;
import com.software.grey.repositories.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepo userRepo;
    private UserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserService(UserRepo userRepo, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepo = userRepo;
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void save(UserDTO userDTO){
        userDTO.password = bCryptPasswordEncoder.encode(userDTO.password);
        User user = User.builder()
                .role(Role.ROLE_USER)
                .tier(Tier.Standard)
                .enabled(true)
                .build();
        user = userMapper.toUser(userDTO, user);
        userRepo.save(user);
    }

    public boolean userExists(UserDTO userDTO){
        return userRepo.findByUsername(userDTO.username) != null || userRepo.findByEmail(userDTO.email) != null;
    }

}
