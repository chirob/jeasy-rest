package com.github.jeasyrest.json.util;

import java.io.IOException;

public interface JsonReader {

    void startJson() throws IOException;

    void endJson() throws IOException;

    void startObject() throws IOException;

    void endObject() throws IOException;

    void startEntry(String key) throws IOException;

    void endEntry() throws IOException;

    void startArray() throws IOException;

    void endArray() throws IOException;

    void primitive(Object value) throws IOException;

}
