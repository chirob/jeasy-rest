package com.github.jeasyrest.test.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.PrivilegedAction;

import javax.security.auth.Subject;

import com.github.jeasyrest.core.security.auth.AuthenticationManager;
import com.github.jeasyrest.ioc.Injections;
import com.github.jeasyrest.test.BaseTest;

public class AuthCoreTest extends BaseTest {

    protected final Method RUN_METHOD;

    {
        Method runMethod;
        try {
            runMethod = getClass().getMethod("run");
        } catch (NoSuchMethodException | SecurityException e) {
            runMethod = null;
        }
        RUN_METHOD = runMethod;
    }

    protected void run(final Method method, final Object targetInstance, boolean auth) {
        if (method != null) {
            logger.info("Authentication enabled: {}", auth);
            if (auth) {
                AuthenticationManager<Void> authManager = Injections.INSTANCE.singleton("authenticationManager");
                Subject subject = authManager.authenticate(null);
                Subject.doAs(subject, new PrivilegedAction<Void>() {
                    @Override
                    public Void run() {
                        execute(method, targetInstance);
                        return null;
                    }
                });
            } else {
                execute(method, targetInstance);
            }
        }
    }

    private void execute(Method method, Object targetInstance) {
        try {
            method.invoke(targetInstance);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
