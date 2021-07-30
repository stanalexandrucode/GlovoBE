package com.cc.glovobe.service.impl;

import com.cc.glovobe.exception.domain.TokenNotFoundException;
import com.cc.glovobe.model.ConfirmationToken;
import com.cc.glovobe.repository.ConfirmationTokenRepository;
import com.cc.glovobe.service.ConfirmationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    public ConfirmationTokenServiceImpl(ConfirmationTokenRepository confirmationTokenRepository) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    @Override
    public ConfirmationToken saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
        return token;
    }

    @Override
    public ConfirmationToken getToken(String token) throws TokenNotFoundException {
        ConfirmationToken confirmationToken = confirmationTokenRepository.findConfirmationTokenByToken(token);
        if (confirmationToken == null) {
            throw new TokenNotFoundException("Token register not found: " + token);
        }
        return confirmationToken;
    }

    @Override
    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(token, LocalDateTime.now());
    }
}
