package com.cc.glovobe.service;

import com.cc.glovobe.exception.domain.TokenNotFoundException;
import com.cc.glovobe.model.ConfirmationToken;
import org.springframework.stereotype.Service;

@Service
public interface ConfirmationTokenService {
    void saveConfirmationToken(ConfirmationToken token);

    ConfirmationToken getToken(String token) throws TokenNotFoundException;

    public int setConfirmedAt(String token);
}
