package org.example.httpserver;

import org.example.httpserver.config.Configuration;
import org.example.httpserver.config.ConfigurationManager;
import org.example.httpserver.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class HttpServer {

//Le logger va nous permettre de pouvoir ouvrir le serveur avec plusieurs navigateur en même temps
    // et de pouvoir réactualiser la page aussi et aussi d'avoir la traçabilité
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        LOGGER.info("Le serveur est entrain de tourner...");

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        LOGGER.info("Il utilise le port : "+ conf.getPort());
        LOGGER.info("Et le chemin : "+conf.getWebroot());

        ServerListenerThread serverListenerThread = null;
        try {
            serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot(), conf.getWebrep());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        serverListenerThread.start();



    }

}
