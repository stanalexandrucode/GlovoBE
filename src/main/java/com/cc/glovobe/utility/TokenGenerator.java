package com.cc.glovobe.utility;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenGenerator {

    public String generateToken() {
        return UUID.randomUUID().toString();
    }


}

