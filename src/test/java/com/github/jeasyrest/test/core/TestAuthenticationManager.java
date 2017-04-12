package com.github.jeasyrest.test.core;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import javax.security.auth.Subject;

import com.github.jeasyrest.core.security.ResourcePrincipal;
import com.github.jeasyrest.core.security.auth.AuthenticationManager;

public class TestAuthenticationManager implements AuthenticationManager<Void> {

    @Override
    public Subject authenticate(Void credentials) {
        return new Subject(true,
                Collections.unmodifiableSet(new HashSet<Principal>(Arrays.asList(new ResourcePrincipal("TEST")))),
                new HashSet<Void>(), new HashSet<Void>());
    }

}
