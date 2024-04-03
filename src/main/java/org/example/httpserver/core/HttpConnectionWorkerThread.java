package org.example.httpserver.core;

import org.example.http.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

import static org.example.http.HttpStatusCode.CLIENT_ERROR_404_PAGE_NOT_FOUND;

public class HttpConnectionWorkerThread extends  Thread {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorkerThread.class);


    private Socket socket;

    public HttpConnectionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String requestLine = reader.readLine();
            if (requestLine != null && requestLine.startsWith("GET")) {
                String[] parts = requestLine.split(" ");
                String path = parts[1];

                File file = new File("." + path);
                if (file.isDirectory()) {
                    String[] files = file.list();
                    StringBuilder responseBuilder = new StringBuilder();
                    responseBuilder.append("HTTP/1.1 200 OK\r\n");
                    responseBuilder.append("Content-Type: text/html\r\n\r\n");
                    responseBuilder.append("<html>");
                          responseBuilder.append("<head>");
                                 responseBuilder.append("<title>").append("Serveur Web HTTP").append("</title>");
                                 responseBuilder.append("<style>");
                                 responseBuilder.append("body { font-family: Arial, sans-serif; }");
                                 responseBuilder.append(".container { width: 80%; margin: 0 auto; padding: 20px; }");
                                 responseBuilder.append("h1 { text-align: center; color: #007bff; }");
                                 responseBuilder.append(".file-list { list-style: none; padding: 0; }");
                                 responseBuilder.append(".file-list li { margin-bottom: 10px; border: 1px solid #ced4da; border-radius: 5px; padding: 10px; background-color: #fff; }");
                                 responseBuilder.append("</style>");
                          responseBuilder.append("</head>");

                          responseBuilder.append("<body>");
                            responseBuilder.append("<div class=\"container\">");
                            responseBuilder.append("<h1>").append("Bienvenue dans notre serveur web HTTP").append("</h1>");
                            responseBuilder.append("<ul class=\"file-list\">");
                            for (String fileName : files) {

                           responseBuilder.append("<li>").append(fileName).append("</li>");
                                //responseBuilder.append("<p><a href=\"/").append(fileName).append("\">").append(fileName).append("</a></p>");

                           }
                           responseBuilder.append("</ul>");
                           responseBuilder.append("</div>");

                    responseBuilder.append("</body>");
                    responseBuilder.append("</html>");
                    outputStream.write(responseBuilder.toString().getBytes());
                } else {

                    if (file.isFile()){

                        FileReader fileReader = new FileReader(file);
                        BufferedReader br = new BufferedReader(fileReader);
                        StringBuffer sb = new StringBuffer();
                        String line;
                        while ((line = br.readLine()) != null){
                            sb.append(line);

                        }
                        outputStream.write(sb.toString().getBytes());
                    }
                    else {
                        HttpStatusCode erreur = CLIENT_ERROR_404_PAGE_NOT_FOUND;
                        StringBuilder responseBuilder = new StringBuilder();
                        responseBuilder.append("<html>");
                             responseBuilder.append("<head>");
                               responseBuilder.append("<title>").append("Serveur Web HTTP").append("</title>");
                               responseBuilder.append("<style>");
                               responseBuilder.append("body { font-family: Arial, sans-serif; }");
                               responseBuilder.append("h1 { text-align: center; color: #007bff; }");
                               responseBuilder.append("</style>");
                             responseBuilder.append("</head>");

                             responseBuilder.append("<body>");
                               responseBuilder.append("<h1>").append(erreur).append("</h1>");
                             responseBuilder.append("</body>");


                        responseBuilder.append("</html>");
                        //responseBuilder.append(erreur);
                        outputStream.write(responseBuilder.toString().getBytes());
                    }

                    LOGGER.info("Traitement de la connexion terminée");
                }
            }
        }catch (IOException e) {
            LOGGER.error("Problème avec la communication", e);
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }
            if (outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {}
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {}
            }

        }


    }
}