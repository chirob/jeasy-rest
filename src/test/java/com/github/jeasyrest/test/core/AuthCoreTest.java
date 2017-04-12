package com.github.jeasyrest.test.core;

import java.security.PrivilegedAction;

import javax.security.auth.Subject;

import com.github.jeasyrest.core.security.auth.AuthenticationManager;
import com.github.jeasyrest.ioc.Injections;
import com.github.jeasyrest.test.BaseTest;

public class AuthCoreTest extends BaseTest {

    protected void run(boolean auth) {
        if (auth) {
            AuthenticationManager<Void> authManager = Injections.INSTANCE.singleton("authenticationManager");
            Subject subject = authManager.authenticate(null);
            Subject.doAs(subject, new PrivilegedAction<Void>() {
                @Override
                public Void run() {
                    AuthCoreTest.this.run();
                    return null;
                }
            });
        } else {
            run();
        }
    }

}