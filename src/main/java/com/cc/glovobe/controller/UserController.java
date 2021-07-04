package com.cc.glovobe.controller;

import com.cc.glovobe.exception.ExceptionHandling;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = { "/", "/user"})
public class UserController extends ExceptionHandling {

}
