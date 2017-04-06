package com.github.chirob.jeasyrest.server;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.sun.net.httpserver.HttpServer;

@SuppressWarnings("restriction")
public class RSServer {

    public static void main(String[] args) throws Exception {
        InetSocketAddress address = toAddress(args);
        HttpServer server = HttpServer.create(address, 0);
        server.createContext("/rs", new HttpServerHandler());
        System.out.println("Starting JeasyREST server at " + address.getHostName() + ":" + address.getPort());
        server.start();
    }

    private static InetSocketAddress toAddress(String[] args) throws UnknownHostException {
        String host = "localhost";
        int port = 8000;
        switch (args.length) {
        case 1:
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                host = args[0];
            }
            break;
        case 2:
            host = args[0];
            port = Integer.parseInt(args[1]);
            break;
        }
        return new InetSocketAddress(InetAddress.getByName(host), port);
    }

}
