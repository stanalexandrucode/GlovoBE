package com.cc.glovobe.service.impl;

import com.cc.glovobe.model.User;
import com.cc.glovobe.repository.UserRepository;
import com.cc.glovobe.service.ConfirmationTokenService;
import com.cc.glovobe.service.EmailService;
import com.cc.glovobe.service.LoginAttemptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.cc.glovobe.enumeration.Role.ROLE_USER;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private EmailService emailService;
    @Mock
    private LoginAttemptService loginAttemptService;

    private UserServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new UserServiceImpl(userRepository, confirmationTokenService,
                bCryptPasswordEncoder, emailService, loginAttemptService);
    }


    @Test
    @Disabled
    void canLoadUserByUsername() {
        
    }

    @Test
    @Disabled
    void register() {
    }

    @Test
    @Disabled
    void confirmToken() {
    }

    @Test
    @Disabled
    void createRegistrationToken() {
    }

    @Test
    @Disabled
    void deleteUserById() {
    }


    @Test
    @Disabled
    void canGetAllUsers() {

    }

    @Test
    @Disabled
    void findUserByEmail() {
    }
}