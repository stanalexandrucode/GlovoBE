package com.cc.glovobe.service.impl;

import com.cc.glovobe.exception.domain.EmailExistException;
import com.cc.glovobe.exception.domain.TokenExpiredException;
import com.cc.glovobe.exception.domain.TokenNotFoundException;
import com.cc.glovobe.model.ConfirmationToken;
import com.cc.glovobe.model.RegistrationRequest;
import com.cc.glovobe.model.User;
import com.cc.glovobe.model.UserPrincipal;
import com.cc.glovobe.prototype.UsersPrototype;
import com.cc.glovobe.repository.UserRepository;
import com.cc.glovobe.service.ConfirmationTokenService;
import com.cc.glovobe.service.EmailService;
import com.cc.glovobe.service.LoginAttemptService;
import com.cc.glovobe.utility.TokenGenerator;
import org.junit.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

import static com.cc.glovobe.constant.UserImplConstant.*;
import static com.cc.glovobe.enumeration.Role.ROLE_USER;
import static com.cc.glovobe.prototype.UsersPrototype.aUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {


    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private UserRepository userRepository;
    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private LoginAttemptService loginAttemptService;
    @Mock
    private TokenGenerator generateToken;


    @InjectMocks
    private UserServiceImpl underTest;

    @BeforeEach
    public void setup() {
    }

    @Test
    void canLoadUserByUsername() {
//      given
        UserDetails userPrincipal = UserPrincipal.builder()
                .user(aUser())
                .build();

        User userUnderTest = new User();
        String usernameEmail = "nelu@gmail.com";
        userUnderTest.setId(1L);
        userUnderTest.setFirstName("Alexandru");
        userUnderTest.setLastName("Stan");
        userUnderTest.setEmail(usernameEmail);
        userUnderTest.setPassword("password");
        userUnderTest.setRole(ROLE_USER.name());
        userUnderTest.setAuthorities(ROLE_USER.getAuthorities());
        userUnderTest.setIsNonLocked(true);
        userUnderTest.setEnabled(false);
        when(userRepository.findUserByEmail(eq(usernameEmail))).thenReturn(userUnderTest);
//        when
        UserDetails userDetails = underTest.loadUserByUsername(usernameEmail);
        //given
        verify(userRepository).findUserByEmail(eq(usernameEmail));
    }


    @Test
    void checkLoadUserByUsernameWhenEmailIsWrong() {
//        given
        String wrongEmail = "wrongEmail";
        when(userRepository.findUserByEmail(wrongEmail)).thenReturn(null);
//        when
//        then
        assertThatThrownBy(() -> underTest.loadUserByUsername(wrongEmail))
                .hasMessage(USER_NOT_FOUND_WITH_EMAIL + wrongEmail)
                .isInstanceOf(UsernameNotFoundException.class);
    }


    @Test
    void checkWhenRegister() throws EmailExistException, MessagingException {
//        given
        when(generateToken.generateToken()).thenReturn("token123");
        RegistrationRequest request = new RegistrationRequest(
                "Alex",
                "Stan",
                "nelu@gmail.com",
                "password");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setIsNonLocked(true);
        user.setEnabled(false);

//      when
        String tok = underTest.register(request);

//      given
        verify(userRepository).save(user);
        verify(emailService).sendAuthenticationLinkConfirmation(eq("token123"), eq(request.getEmail()));
        assertThat(tok).isEqualTo("token123");

    }


    @Test
    void checkWhenTokenIsWrongCanRegister() throws EmailExistException, MessagingException {
//        given
        when(generateToken.generateToken()).thenReturn("token123");
        RegistrationRequest request = new RegistrationRequest(
                "Alex",
                "Stan",
                "nelu@gmail.com",
                "password");
        User user = new User();
        user.setEmail(request.getEmail());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setIsNonLocked(true);
        user.setEnabled(false);

//      when
        String tok = underTest.register(request);

//      given
        verify(userRepository).save(user);
        verify(emailService).sendAuthenticationLinkConfirmation(eq("token123"), eq(request.getEmail()));
        assertThat(tok).isNotEqualTo("wrongEmail");
    }

    @Test
    void checkRegistrationWillThrowWhenEmailExist() throws EmailExistException {
//        given
        RegistrationRequest request = new RegistrationRequest(
                "Alex",
                "Stan",
                "nelu@gmail.com",
                "password");
        given(underTest.validateNewEmail(eq("nelu@gmail.com"))).willAnswer(invocation -> {
            throw new EmailExistException(EMAIL_ALREADY_EXIST + request.getEmail());
        });
//        when
//        then
        assertThatThrownBy(() -> underTest.register(request))
                .hasMessage(EMAIL_ALREADY_EXIST + request.getEmail())
                .isInstanceOf(EmailExistException.class);

    }

//    @Test
//    void checkRegistrationWillThrowWhenMessagingException() throws MessagingException {
////        given
//        String token = "asdsad";
//        RegistrationRequest request = new RegistrationRequest(
//                "Alex",
//                "Stan",
//                "nelu@gmail.com",
//                "password");
//    doThrow(new MessagingException("Error occurred")).when(emailService).sendAuthenticationLinkConfirmation(token, request.getEmail());
//        emailService.sendAuthenticationLinkConfirmation(token, request.getEmail());
////        when
////        then
//        assertThatThrownBy(() -> underTest.register(request))
//                .isInstanceOf(MessagingException.class);
//    }

    @Test
    void canConfirmToken() throws TokenNotFoundException, EmailExistException, TokenExpiredException {

    }


    @Test
    void canCreateRegistrationToken() {
        User user = aUser();
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user);


        when(userRepository.save(user)).thenReturn(user);
        userRepository.save(user);

        when(confirmationTokenService.saveConfirmationToken(confirmationToken)).thenReturn(confirmationToken);
        ConfirmationToken confirmationToken1 = confirmationTokenService.saveConfirmationToken(confirmationToken);

        assertThat(confirmationToken1).isEqualTo(confirmationToken);
    }

    @Test
    void willThrowWhenTokenNotFoundException() throws EmailExistException, TokenExpiredException, TokenNotFoundException {
        String token = "qwerrty";

        exceptionRule.expect(TokenNotFoundException.class);
        exceptionRule.expectMessage(TOKEN_REGISTRATION_NOT_FOUND + token);
        when(confirmationTokenService.getToken(token)).thenReturn(null);
        underTest.confirmToken(token);
    }


    @Test
    void willThrowWhenEmailExistException() throws EmailExistException, TokenExpiredException, TokenNotFoundException {
        String token = "qwerrty";
        ConfirmationToken confirmationTokenBuild = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().minus(Period.ofDays(10)),
                UsersPrototype.aUser()

        );

        exceptionRule.expect(EmailExistException.class);
        exceptionRule.expectMessage(EMAIL_ALREADY_CONFIRMED);
        when(confirmationTokenService.getToken(token)).thenReturn(confirmationTokenBuild);
        confirmationTokenService.getToken(token);
        when(confirmationTokenService.getToken(token).getConfirmedAt()).thenReturn(null);
        underTest.confirmToken(token);
    }

    @Test
    void willThrowWhenTokenExpiredException() throws EmailExistException, TokenExpiredException, TokenNotFoundException {
        String token = "qwerrty";
        ConfirmationToken confirmationTokenBuild = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().minus(Period.ofDays(10)),
                UsersPrototype.aUser()

        );

        exceptionRule.expect(TokenExpiredException.class);
        exceptionRule.expectMessage(TOKEN_EXPIRED + token);
        when(confirmationTokenService.getToken(token)).thenReturn(confirmationTokenBuild);
        confirmationTokenService.getToken(token);
        underTest.confirmToken(token);

    }

    @Test
    void canDeleteUserById() {
//        when
        underTest = new UserServiceImpl(userRepository, confirmationTokenService, bCryptPasswordEncoder, emailService, loginAttemptService, generateToken);
        underTest.deleteUserById(1L);
//        given
        verify(userRepository).deleteUserById(1L);

    }

    @Test
    void canFindUserByEmail() {
//        given
        String email = "asdsadsd";
        underTest = new UserServiceImpl(userRepository, confirmationTokenService, bCryptPasswordEncoder, emailService, loginAttemptService, generateToken);
        underTest.findUserByEmail(email);
//        when
        verify(userRepository).findUserByEmail(email);
    }


}