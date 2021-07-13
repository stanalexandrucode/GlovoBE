package com.cc.glovobe.service.impl;

import com.cc.glovobe.exception.domain.EmailExistException;
import com.cc.glovobe.exception.domain.TokenExpiredException;
import com.cc.glovobe.exception.domain.TokenNotFoundException;
import com.cc.glovobe.model.ConfirmationToken;
import com.cc.glovobe.model.RegistrationRequest;
import com.cc.glovobe.model.User;
import com.cc.glovobe.model.UserPrincipal;
import com.cc.glovobe.repository.UserRepository;
import com.cc.glovobe.service.ConfirmationTokenService;
import com.cc.glovobe.service.EmailService;
import com.cc.glovobe.service.LoginAttemptService;
import com.cc.glovobe.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.cc.glovobe.constant.UserImplConstant.*;
import static com.cc.glovobe.enumeration.Role.ROLE_USER;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    private Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final ConfirmationTokenService confirmationTokenService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final LoginAttemptService loginAttemptService;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, ConfirmationTokenService confirmationTokenService, BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService, LoginAttemptService loginAttemptService) {
        this.userRepository = userRepository;
        this.confirmationTokenService = confirmationTokenService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.loginAttemptService = loginAttemptService;
    }


    @Override
    public UserDetails loadUserByUsername(String usernameEmail) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(usernameEmail);
        if (user == null || !user.getEnabled()) {
            LOGGER.error(USER_NOT_FOUND_WITH_EMAIL + usernameEmail);
            throw new UsernameNotFoundException(USER_NOT_FOUND_WITH_EMAIL + usernameEmail);
        } else {
            validateLoginAttempt(user);
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(RETURNING_FOUND_USER_WITH_EMAIL + usernameEmail);
            return userPrincipal;
        }
    }


    @Override
    public String register(RegistrationRequest request) throws EmailExistException, MessagingException {
        validateNewEmail(request.getEmail());
        User user = new User();
        user.setEmail(request.getEmail());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());
        user.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        user.setRole(ROLE_USER.name());
        user.setAuthorities(ROLE_USER.getAuthorities());
        user.setIsNonLocked(true);
        user.setEnabled(false);
        String token = createRegistrationToken(user);
        emailService.sendAuthenticationLinkConfirmation(token, request.getEmail());
        return token;
    }

    private User validateNewEmail(String email) throws EmailExistException {
        User userByEmail = findUserByEmail(email);
        if (userByEmail != null) {
            throw new EmailExistException(EMAIL_ALREADY_EXIST + email);
        }
        return null;
    }


    @Transactional
    public String confirmToken(String token) throws TokenNotFoundException, EmailExistException, TokenExpiredException {

        //verify token if exists
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token);
        if (confirmationToken == null) {
            throw new TokenNotFoundException(TOKEN_REGISTRATION_NOT_FOUND + token);
        }

        //verify token if used
        if (confirmationToken.getConfirmedAt() != null) {
            throw new EmailExistException(EMAIL_ALREADY_CONFIRMED);
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        //verify token if expired
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException(TOKEN_EXPIRED + token);
        }

        confirmationTokenService.setConfirmedAt(token);
        enableUser(confirmationToken.getUser().getEmail());
        return REGISTRATION_COMPLETE_MESSAGE;
    }


    @Transactional
    public String createRegistrationToken(User user) {
//      save user in DB
        userRepository.save(user);

//      create token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                user);

//       save, in DB, AND return auth token
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    private void validateLoginAttempt(User user) {
        if (user.getIsNonLocked()) {
            if (loginAttemptService.hasExceededMaxAttempts(user.getEmail())) {
                user.setIsNonLocked(false);
            } else {
                user.setIsNonLocked(true);
            }
        } else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getEmail());
        }
    }

    public int enableUser(String email) {
        return userRepository.enableUser(email);
    }

    @Override
    public List<User> getUsers() {
        return null;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
