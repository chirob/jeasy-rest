package com.github.jeasyrest.io.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.Properties;

@SuppressWarnings("serial")
public class LoadedProperties extends Properties {

    public LoadedProperties(InputStream in) throws IOException {
        try {
            load(in);
        } finally {
            IOUtils.close(in);
        }
    }

    public LoadedProperties(Reader in) throws IOException {
        try {
            load(in);
        } finally {
            IOUtils.close(in);
        }
    }

    public LoadedProperties(URL url) throws IOException {
        this(url.openStream());
    }

}
