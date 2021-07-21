package com.cc.glovobe.controller;

import com.cc.glovobe.exception.ExceptionHandling;
import com.cc.glovobe.exception.domain.*;
import com.cc.glovobe.model.*;
import com.cc.glovobe.service.UserService;
import com.cc.glovobe.utility.JWTTokenProvider;
import com.sun.xml.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import static com.cc.glovobe.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(path = {"/user"})
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
    public String confirm(@RequestParam("token") String token) throws TokenNotFoundException, EmailExistException, TokenExpiredException {
        return userService.confirmToken(token);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequest data) {
        authenticate(data.getEmail(), data.getPassword());
        User loginUser = userService.findUserByEmail(data.getEmail());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
        return response(OK, "User successfully deleted");
    }


    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        // Headers not showing in fetch response without
        headers.add("access-control-expose-headers", "JwtToken");
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }


    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }


}
