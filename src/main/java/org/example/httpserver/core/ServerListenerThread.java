package org.example.httpserver.core;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);


    private int port;
    private String webroot;
    private String webrep;

    private ServerSocket serverSocket;

    public ServerListenerThread(int port, String webroot, String webrep) throws IOException {
        this.port = port;
        this.webroot = webroot;
        this.webrep = webrep;
        this.serverSocket = new ServerSocket(this.port);

    }

    @Override
    public void run() {
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                LOGGER.info("Connexion acceptée: " + socket.getInetAddress());

                HttpConnectionWorkerThread workerThread = new HttpConnectionWorkerThread(socket);
                workerThread.start();

            }

        } catch (IOException ex) {
            LOGGER.error("Problème avec le paramétrage du socket", ex);
        }finally {
            if (serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {

                }
            }
        }

    }
}



