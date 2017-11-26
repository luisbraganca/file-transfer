import Net.DirectDataTransmission;
import Net.ReceptorList;
import Net.Transmission;
import Util.Logger;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.File;
import java.io.IOException;

public class Main {

    private static final int PORT = 6000;

    public static void main(String[] args) {
        try {
            Logger.log("Reading keystore...");
            System.setProperty("javax.net.ssl.keyStore", "keyStore" + File.separatorChar + "keystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", "storePassword");
            Logger.log("Keystore setup completed.");
            Logger.log("Initializing server...");
            SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(PORT);
            Logger.log("Server ready.");
            // Start console here
            while (true) {
                try {
                    Logger.log("Waiting for connections...");
                    SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
                    Logger.log(sslSocket.getInetAddress().getHostAddress() + " connected.");
                    new Thread(() -> {
                        try {
                            handleClient(sslSocket);
                        } catch (Exception e) {
                            Logger.log("Something wrong happened at " + sslSocket.getInetAddress().getHostAddress() + ": '" + e.getMessage() + "', aborting.");
                            try {
                                sslSocket.close();
                            } catch (IOException ignored) {
                            }
                        }
                    }).start();
                } catch (IOException f) {
                    Logger.log(f.getMessage());
                }
            }
        } catch (IOException e) {
            Logger.log(e.getMessage());
        }
    }

    private static void handleClient(SSLSocket sslSocket) throws IOException {
        String clientType;
        try {
            clientType = new Transmission(sslSocket).readText();
        } catch (IOException e) {
            Logger.log("Failed retreiving text from " + sslSocket.getInetAddress().getHostAddress() + ", problably not a secure connection, aborting.");
            sslSocket.close();
            return;
        }
        if (clientType.toUpperCase().equals("SENDER")) {
            Logger.log(sslSocket.getInetAddress().getHostAddress() + " is a sender.");
            handleSender(sslSocket);
        } else if (clientType.toUpperCase().equals("RECEPTOR")) {
            Logger.log(sslSocket.getInetAddress().getHostAddress() + " is a receptor.");
            handleReceptor(sslSocket);
        } else {
            Logger.log(sslSocket.getInetAddress().getHostAddress() + " isn't a secure connection, aborting connection.");
            sslSocket.close();
        }
    }

    private static void handleReceptor(SSLSocket sslSocket) throws IOException {
        String generatedId = ReceptorList.addReceptor(sslSocket);
        Logger.log(sslSocket.getInetAddress().getHostAddress() + " was given the id " + generatedId);
        new Transmission(sslSocket).sendText(generatedId);
        boolean canceled = false;
        while (!canceled) {
            switch (new Transmission(sslSocket).readText(0)) {
                case "CANCEL":
                    Logger.log(sslSocket.getInetAddress().getHostAddress() + " (" + generatedId + ") canceled its file reception.");
                    canceled = true;
                    break;
                default:
                    break;
            }
        }
        ReceptorList.removeReceptor(generatedId);
    }

    private static void handleSender(SSLSocket sslSocket) throws IOException {
        new Transmission(sslSocket).sendText("RECEPTOR_ID?");
        boolean validId = false;
        String id;
        do {
            id = new Transmission(sslSocket).readText();
            Logger.log(sslSocket.getInetAddress().getHostAddress() + " wants to send file to id " + id);
            if (ReceptorList.existsId(id)) {
                new Transmission(sslSocket).sendText("ID_IS_VALID");
                validId = true;
            } else {
                Logger.log("But the id " + id + " is not a valid id.");
                new Transmission(sslSocket).sendText("NOT_A_VALID_ID");
            }
        } while (!validId);
        Logger.log(id + " exists and corresponds to " + ReceptorList.getSocket(id).getInetAddress().getHostAddress() + ", sending file from " + sslSocket.getInetAddress().getHostAddress());
        new DirectDataTransmission(sslSocket, ReceptorList.getSocket(id)).start();
        Logger.log("Data transmission from " + sslSocket.getInetAddress().getHostAddress() + " to " + ReceptorList.getSocket(id).getInetAddress().getHostAddress() + " (" + id + ") finished, closing.");
        ReceptorList.removeReceptor(id);
        sslSocket.close();
    }
}
