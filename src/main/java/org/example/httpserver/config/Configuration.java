package org.example.httpserver.config;

public class Configuration {

    private int port;
    private String webroot;
    private String webrep;

    public int getPort() {
        return port;
    }


    public void setPort(int port) {
        this.port = port;
    }

    public String getWebroot() {
        return webroot;
    }

    public void setWebroot(String webroot) {
        this.webroot = webroot;
    }

    public String getWebrep() {
        return webrep;
    }

    public void setWebrep(String webrep) {
        this.webrep = webrep;
    }
}
