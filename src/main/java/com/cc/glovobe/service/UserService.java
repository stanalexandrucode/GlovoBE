package com.cc.glovobe.service;

import com.cc.glovobe.exception.domain.EmailExistException;
import com.cc.glovobe.exception.domain.UserNotFoundException;
import com.cc.glovobe.exception.domain.UsernameExistException;
import com.cc.glovobe.model.RegistrationRequest;
import com.cc.glovobe.model.User;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User register(RegistrationRequest request) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException;

    List<User> getUsers();

    User findUserByEmail(String email);

}
