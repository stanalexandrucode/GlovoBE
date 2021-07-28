package com.cc.glovobe.service;

import com.cc.glovobe.exception.domain.TokenNotFoundException;
import com.cc.glovobe.model.ConfirmationToken;



public interface ConfirmationTokenService {
    ConfirmationToken saveConfirmationToken(ConfirmationToken token);

    ConfirmationToken getToken(String token) throws TokenNotFoundException;

    int setConfirmedAt(String token);
}
