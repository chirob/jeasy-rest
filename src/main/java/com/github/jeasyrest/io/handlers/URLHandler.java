package com.github.jeasyrest.io.handlers;

import java.io.IOException;
import java.net.URL;

public class URLHandler extends URLConnectionHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) throws IOException {
        super.init(((URL) sourceObject).openConnection(), sourceParams);
    }

}
