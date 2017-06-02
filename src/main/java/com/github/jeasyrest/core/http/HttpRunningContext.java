package com.github.jeasyrest.core.http;

import java.net.HttpURLConnection;
import java.net.URLConnection;

import com.github.jeasyrest.core.impl.RunningContext;

class HttpRunningContext extends RunningContext {

    private static final HttpRunningContext INSTANCE = new HttpRunningContext();

    static void init(HttpURLConnection connection) {
        INSTANCE.put(URLConnection.class, connection);
    }

    private HttpRunningContext() {
    }

}
