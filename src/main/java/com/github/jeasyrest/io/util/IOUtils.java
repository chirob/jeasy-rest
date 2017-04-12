package com.github.jeasyrest.io.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class IOUtils {

    public static final <T> T throwIOException(Exception e) throws IOException {
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        } else if (e instanceof IOException) {
            throw (IOException) e;
        } else {
            throw new IOException(e);
        }      
    }
    
    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public static void write(InputStream in, boolean closeIn, OutputStream out, boolean closeOut) throws IOException {
        write(512, in, closeIn, out, closeOut);
    }

    public static void write(Reader in, boolean closeIn, Writer out, boolean closeOut) throws IOException {
        write(512, in, closeIn, out, closeOut);
    }

    public static void write(int bufferLenght, InputStream in, boolean closeIn, OutputStream out, boolean closeOut)
            throws IOException {
        try {
            byte[] buffer = new byte[bufferLenght];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            closeIn = true;
            closeOut = true;
        } finally {
            if (closeIn) {
                close(in);
            }
            if (closeOut) {
                close(out);
            }
        }
    }

    public static void write(int bufferLenght, Reader in, boolean closeIn, Writer out, boolean closeOut)
            throws IOException {
        try {
            char[] buffer = new char[bufferLenght];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (Exception e) {
            closeIn = true;
            closeOut = true;
        } finally {
            if (closeIn) {
                close(in);
            }
            if (closeOut) {
                close(out);
            }
        }
    }

}
