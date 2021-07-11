package com.cc.glovobe.service;

import com.cc.glovobe.exception.domain.*;
import com.cc.glovobe.model.RegistrationRequest;
import com.cc.glovobe.model.User;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;


import java.util.List;


public interface UserService {
    String register(RegistrationRequest request) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, javax.mail.MessagingException;

    List<User> getUsers();

    User findUserByEmail(String email);

    String confirmToken(String token) throws TokenNotFoundException, EmailExistException, TokenExpiredException;

}
