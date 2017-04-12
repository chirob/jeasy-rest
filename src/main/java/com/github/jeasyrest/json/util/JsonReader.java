package com.github.jeasyrest.json.util;

import java.io.IOException;
import java.io.Reader;

public class JsonReader {

    public void parse(Reader reader) throws IOException {
        sb.delete(0, sb.length());
        char[] buffer = new char[1024];
        int rl = 0;
        while ((rl = reader.read(buffer)) != -1) {
            parse(buffer, 0, rl);
        }
    }

    public void startJson() throws IOException {
    }

    public void endJson() throws IOException {
    }

    public void startObject() throws IOException {
    }

    public void endObject() throws IOException {
    }

    public void startEntry(String key) throws IOException {
    }

    public void endEntry() throws IOException {
    }

    public void startArray() throws IOException {
    }

    public void endArray() throws IOException {
    }

    public void primitive(Object value) throws IOException {
    }

    private void parse(char[] chars, int off, int len) throws IOException {
        String line = new String(chars, off, len);
        boolean startJson = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            switch (ch) {
            case '{':
                if (!startJson) {
                    startJson();
                    startJson = true;
                }
                startObject();
                clearBuffer();
                break;
            case '[':
                if (!startJson) {
                    startJson();
                    startJson = true;
                }
                startArray();
                clearBuffer();
                break;
            case '}':
                checkEndEntry();
                endObject();
                break;
            case ']':
                checkEndEntry();
                endArray();
                break;
            case ',':
                checkEndEntry();
                break;
            case ':':
                checkStartEntry();
                break;
            default:
                sb.append(ch);
            }
        }
        if (startJson) {
            endJson();
        }
    }

    private void checkStartEntry() throws IOException {
        String key = getBufferedValue();
        if (key != null) {
            startEntry(key);
        }
        clearBuffer();
    }

    private void checkEndEntry() throws IOException {
        String value = getBufferedValue();
        if (value != null) {
            primitive(value);
            endEntry();
        }
        clearBuffer();
    }

    private String getBufferedValue() {
        String value = sb.toString().trim();
        if (value.length() != 0) {
            if (value.matches("\".+\"")) {
                value = value.substring(1, value.length() - 1);
            }
            return value;
        } else {
            return null;
        }
    }

    private void clearBuffer() {
        sb.delete(0, sb.length());
    }

    private StringBuilder sb = new StringBuilder();

}
