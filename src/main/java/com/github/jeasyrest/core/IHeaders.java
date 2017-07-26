package com.github.jeasyrest.core;

import java.util.Map;

public interface IHeaders extends Map<String, Iterable<String>> {

    Iterable<String> put(String key, String... values);
    
}
