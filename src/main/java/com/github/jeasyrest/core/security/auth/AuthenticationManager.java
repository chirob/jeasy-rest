package com.github.jeasyrest.core.security.auth;

import javax.security.auth.Subject;

public interface AuthenticationManager<T> {

    Subject authenticate(T credentials);
    
}
