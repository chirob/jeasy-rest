package com.github.jeasyrest.json.util;

import java.io.IOException;
import java.io.Reader;

public class JsonParser {

    public JsonParser(JsonReader jsonReader) {
        this.jsonReader = jsonReader;
    }

    public void parse(Reader reader) throws IOException {
        sb.delete(0, sb.length());
        char[] buffer = new char[1024];
        int rl = 0;
        while ((rl = reader.read(buffer)) != -1) {
            parse(buffer, 0, rl);
        }
    }

    protected void parse(char[] chars, int off, int len) throws IOException {
        String line = new String(chars, off, len);
        boolean startJson = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            switch (ch) {
            case '{':
                if (!startJson) {
                    jsonReader.startJson();
                    startJson = true;
                }
                jsonReader.startObject();
                clearBuffer();
                break;
            case '[':
                if (!startJson) {
                    jsonReader.startJson();
                    startJson = true;
                }
                jsonReader.startArray();
                clearBuffer();
                break;
            case '}':
                checkEndEntry();
                jsonReader.endObject();
                break;
            case ']':
                checkEndEntry();
                jsonReader.endArray();
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
            jsonReader.endJson();
        }
    }

    private void checkStartEntry() throws IOException {
        String key = getBufferedValue();
        if (key != null) {
            jsonReader.startEntry(key);
        }
        clearBuffer();
    }

    private void checkEndEntry() throws IOException {
        String value = getBufferedValue();
        if (value != null) {
            jsonReader.primitive(value);
            jsonReader.endEntry();
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
    private JsonReader jsonReader;

}