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
        userUnderTest.setEnabled(true);

        UserPrincipal userPrincipal = new UserPrincipal(userUnderTest);


        when(userRepository.findUserByEmail(eq(usernameEmail))).thenReturn(userUnderTest);
//        when
        UserPrincipal userDetails = (UserPrincipal) underTest.loadUserByUsername(usernameEmail);
        //given
        verify(userRepository).findUserByEmail(eq(usernameEmail));
        verify(userRepository).save(userUnderTest);

//        assertThat(userDetails).isEqualTo(userPrincipal);
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


    @Test
    void canCreateRegistrationToken() {
        //given
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
        //when
        ConfirmationToken confirmationToken1 = confirmationTokenService.saveConfirmationToken(confirmationToken);
        //then
        assertThat(confirmationToken1).isEqualTo(confirmationToken);
    }

    @Test
    void checkConfirmationToken() throws EmailExistException, TokenExpiredException, TokenNotFoundException {
//      given
        when(generateToken.generateToken()).thenReturn("token123");
        String generatedToken = generateToken.generateToken();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                generatedToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                aUser());

        when(confirmationTokenService.getToken(generatedToken)).thenReturn(confirmationToken);
        when(userRepository.findUserByEmail(any())).thenReturn(aUser());
        User userUnderTest = userRepository.findUserByEmail(any());
//      when
        String registrationCompleted = underTest.confirmToken(generatedToken);

//      then
        verify(confirmationTokenService).getToken(generatedToken);
        verify(confirmationTokenService).setConfirmedAt(generatedToken);
        verify(userRepository).save(userUnderTest);

        assertThat(registrationCompleted).isEqualTo(REGISTRATION_COMPLETE_MESSAGE);
    }


    @Test
    void willThrowWhenEmailExistExceptionInConfirmToken() throws TokenNotFoundException {
//        given
        when(generateToken.generateToken()).thenReturn("token123");
        String generatedToken = generateToken.generateToken();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                1L,
                generatedToken,
                LocalDateTime.now(),
                LocalDateTime.now().minus(Period.ofDays(10)),
                LocalDateTime.now(),
                UsersPrototype.aUser()

        );
        when(confirmationTokenService.getToken(generatedToken)).thenReturn(confirmationToken);
//        when
//        then
        assertThatThrownBy(() -> underTest.confirmToken(generatedToken))
                .hasMessage(EMAIL_ALREADY_CONFIRMED)
                .isInstanceOf(EmailExistException.class);
    }

    @Test
    void willThrowWhenTokenNotFoundException() throws TokenNotFoundException {
        when(generateToken.generateToken()).thenReturn("token123");
        String generatedToken = generateToken.generateToken();
        when(confirmationTokenService.getToken(generatedToken)).thenReturn(null);
//        when
//        then
        assertThatThrownBy(() -> underTest.confirmToken(generatedToken))
                .hasMessage(TOKEN_REGISTRATION_NOT_FOUND + generatedToken)
                .isInstanceOf(TokenNotFoundException.class);
    }

    @Test
    void willThrowWhenTokenExpiredException() throws TokenNotFoundException {
//        given
        when(generateToken.generateToken()).thenReturn("token123");
        String generatedToken = generateToken.generateToken();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                generatedToken,
                LocalDateTime.now(),
                LocalDateTime.now().minusMinutes(15),
                aUser());
        when(confirmationTokenService.getToken(generatedToken)).thenReturn(confirmationToken);
//        when
//        then
        assertThatThrownBy(() -> underTest.confirmToken(generatedToken))
                .hasMessage(TOKEN_EXPIRED + generatedToken)
                .isInstanceOf(TokenExpiredException.class);
    }


    @Test
    void canDeleteUserById() {
//        given
//        when
        underTest.deleteUserById(1L);
//        then
        verify(userRepository).deleteUserById(1L);

    }

    @Test
    void canFindUserByEmail() {
//        given
//        when
        String email = "asdsadsd";
        underTest.findUserByEmail(email);
//        given
        verify(userRepository).findUserByEmail(email);
    }


}