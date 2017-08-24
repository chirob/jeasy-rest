package com.github.jeasyrest.core.process.json.util;

public class Capitalizer {

    public static final String firstCharToLowerCase(String s) {
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }
    
}
