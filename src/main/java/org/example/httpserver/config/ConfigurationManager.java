package org.example.httpserver.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.example.httpserver.util.Json;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ConfigurationManager {

    private static ConfigurationManager maConfiguration;

    private static Configuration maConfigurationActuelle;
    private ConfigurationManager(){

    }

    public static ConfigurationManager getInstance() {
        if (maConfiguration == null)
            maConfiguration = new ConfigurationManager();
        return maConfiguration;
    }

    /**
     * Téléchargement d'un fichier de configuration à partir du chemin fourni
     */
    public void loadConfigurationFile(String filePath) {
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new HttpConfigurationException(e);
        }
        StringBuffer sb = new StringBuffer();
        int i;
        while (true) {
            try {
                if (!(( i = fileReader.read()) != -1)) break;
            } catch (IOException e) {
                throw new HttpConfigurationException(e);
            }
            sb.append((char)i);
        }
        JsonNode conf = null;
        try {
            conf = Json.parse(sb.toString());
        } catch (IOException e) {
            throw new HttpConfigurationException("Erreur lors de l'analyse de la configuration", e);
        }
        try {
            maConfigurationActuelle = Json.fromJson(conf , Configuration.class);
        } catch (JsonProcessingException e) {
            throw new HttpConfigurationException("Erreur lors de l'analyse de la configuration interne",e);
        }

    }

    /**
     * Renvoie la configuration actuellement chargée
     */
    public Configuration getCurrentConfiguration(){
        if (maConfigurationActuelle == null) {
           throw new HttpConfigurationException("Pas de configuration actuelle renseignée");
        }
     return maConfigurationActuelle;
    }
}
