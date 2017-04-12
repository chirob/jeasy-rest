package com.github.jeasyrest.io.handlers;

import java.io.IOException;
import java.net.URI;

public class URIHandler extends URLHandler {

    @Override
    public void init(Object sourceObject, Object... sourceParams) throws IOException {
        super.init(((URI) sourceObject).toURL(), sourceParams);
    }

}
