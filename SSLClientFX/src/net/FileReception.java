/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

/**
 * @author lbsilva
 */
public class FileReception {

    private final static String SERVER_IP = "5.249.23.60";
    private final static int SERVER_PORT = 6000;
    private final static int MAX_BUFFER_SIZE = 1024 * 16;

    private boolean canceled;
    private String status;
    private File fileToReceive;
    private SSLSocket sslSocket;
    private String id;

    public FileReception(String path) throws IOException {
        if (path.isEmpty()) {
            throw new RuntimeException("Empty file path");
        }
        this.fileToReceive = new File(path);
//        if (fileToReceive.exists()) {    CASO NÃO SE PERMITA A SUBSTITUIÇÃO, DESCOMENTAR ISTO
//            throw new FileAlreadyExistsException("File \""+path+"\" already exists.");
//        }
        canceled = false;
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        sslSocket = (SSLSocket) sslSocketFactory.createSocket(SERVER_IP, SERVER_PORT);
        handshake();
    }

    private void handshake() throws IOException {
        new Transmission(sslSocket).sendText("RECEPTOR");
        this.id = new Transmission(sslSocket).readText();
    }

    public void closeConnection() {
        canceled = true;
        try {
            new Transmission(sslSocket).sendText("CANCEL");
        } catch (IOException ignored) {
        }
        try {
            sslSocket.close();
        } catch (IOException ignored) {
        }
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void start() {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        InputStream in = null;
        try {
            sslSocket.setSoTimeout(0);
            in = sslSocket.getInputStream();
            fos = new FileOutputStream(fileToReceive);
            bos = new BufferedOutputStream(fos);

            byte[] buffer = new byte[MAX_BUFFER_SIZE];
            int length;
            while (!canceled && ((length = in.read(buffer)) > 0)) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            status = "Downloaded\n\"" + fileToReceive.getAbsolutePath()
                    + "\"\n(" + fileToReceive.length() + " bytes)";
        } catch (IOException exception) {
            status = "Canceled";
            canceled = true;
        } finally {
            finish(fos, bos, in, sslSocket);
        }
    }

    /**
     * Finishes a transformation process by closing all the streams and
     * deleting the file if it was aborted
     * @param closeables the streams
     */
    private void finish(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
        if (canceled) fileToReceive.delete(); // If it was aborted the file might be corrupted
    }
}
