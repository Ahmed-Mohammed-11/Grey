package com.software.grey.TestRepositories;


import com.software.grey.TestDataUtil.ObjectsBuilder;
import com.software.grey.models.entities.User;
import com.software.grey.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserRepositoryTest {
    private final UserRepository userRepository;
    private final ObjectsBuilder objectsBuilder;

    @Autowired
    public UserRepositoryTest(final UserRepository userRepository, ObjectsBuilder objectsBuilder) {
        this.userRepository = userRepository;
        this.objectsBuilder = objectsBuilder;
    }

    @Test
    void TestSaveUser() {
        User user = objectsBuilder.createUserA();
        userRepository.save(user);
        Optional<User> result = userRepository.findById(user.getId());
        assertTrue(result.isPresent());
        User res = result.get();
        assertEquals(res.getPassword(), user.getPassword());
        assertEquals(res.getEmail(), user.getEmail());
        assertEquals(res.getId(), user.getId());
        assertEquals(res.getUsername(), user.getUsername());
    }

    @Test
    void TestFindById() {
        User user = objectsBuilder.createUserA();
        userRepository.save(user);
        Optional<User> result = userRepository.findById(user.getId());
        User res = result.get();
        assertEquals(res.getPassword(), user.getPassword());
        assertEquals(res.getEmail(), user.getEmail());
        assertEquals(res.getId(), user.getId());
        assertEquals(res.getUsername(), user.getUsername());
    }
}
