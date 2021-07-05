package com.cc.glovobe.controller;

import com.cc.glovobe.exception.ExceptionHandling;
import com.cc.glovobe.exception.domain.EmailExistException;
import com.cc.glovobe.exception.domain.TokenNotFoundException;
import com.cc.glovobe.exception.domain.UserNotFoundException;
import com.cc.glovobe.exception.domain.UsernameExistException;
import com.cc.glovobe.model.RegistrationRequest;
import com.cc.glovobe.model.User;
import com.cc.glovobe.service.UserService;
import com.cc.glovobe.utility.JWTTokenProvider;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = {"/", "/user"})
public class UserController extends ExceptionHandling {
    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserService userService, JWTTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) throws UserNotFoundException, EmailExistException, MessagingException, javax.mail.MessagingException, UsernameExistException {
        String token = userService.register(request);
        return new ResponseEntity<>(token, OK);
    }

    @GetMapping(path = "/confirm")
    public String confirm(@RequestParam("token") String token) throws TokenNotFoundException {
        return userService.confirmToken(token);
    }
//    @PostMapping("/login")
//    public ResponseEntity<User> login(@RequestBody RegistrationRequest request) throws UserNotFoundException, EmailExistException, MessagingException, javax.mail.MessagingException, UsernameExistException {
//
//        return new ResponseEntity<>(token, OK);
//    }

}
