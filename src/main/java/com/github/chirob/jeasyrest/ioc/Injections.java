package com.github.chirob.jeasyrest.ioc;

import com.github.chirob.jeasyrest.ioc.util.InjectionMap;

public class Injections extends InjectionMap {

    public static final Injections INSTANCE = new Injections();
    
    private Injections(String... mapNames) {
        super("META-INF/jeasyrest/injections", "jeasyrest/injections");
    }

}
