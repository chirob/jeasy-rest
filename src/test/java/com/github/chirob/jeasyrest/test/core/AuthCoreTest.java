package com.github.chirob.jeasyrest.test.core;

import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import javax.security.auth.Subject;

import com.github.chirob.jeasyrest.core.security.ResourcePrincipal;
import com.github.chirob.jeasyrest.core.security.auth.AuthenticationManager;
import com.github.chirob.jeasyrest.test.BaseTest;

public class AuthCoreTest extends BaseTest {

    protected void run() {
        AuthenticationManager<Void> authManager = new AuthenticationManager<Void>() {
            @Override
            public Subject authenticate(Void credentials) {
                return new Subject(true,
                        Collections
                                .unmodifiableSet(new HashSet<Principal>(Arrays.asList(new ResourcePrincipal("TEST")))),
                        new HashSet<Void>(), new HashSet<Void>());
            }
        };
        Subject subject = authManager.authenticate(null);

        Subject.doAs(subject, new PrivilegedAction<Void>() {
            @Override
            public Void run() {
                AuthCoreTest.super.run();
                return null;
            }
        });
    }
    
}
