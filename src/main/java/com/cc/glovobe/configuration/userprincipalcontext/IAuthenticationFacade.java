package com.cc.glovobe.configuration.userprincipalcontext;

import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();
}
