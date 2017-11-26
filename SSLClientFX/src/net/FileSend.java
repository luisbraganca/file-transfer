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
public class FileSend {

    /**
     *
     */
    private final static String SERVER_IP = "5.249.23.60";

    /**
     * Socket port to use when transfering files
     */
    private final static int SERVER_PORT = 6000;

    /**
     * Buffer size for file transfer
     */
    private final static int BUFFER_SIZE = 1024 * 16;

    /**
     * The file that will be sent
     */
    private File fileToSend;

    /**
     * The sslSocket to be used
     */
    private SSLSocket sslSocket;

    /**
     * If sending was canceled by the user
     */
    private boolean canceled;

    /**
     * Current status of file transfer
     */
    private String status;

    /**
     * A Thread, ready to send a file to a certain IP with an available status property
     *
     * @param path - Path of the file to be sent
     * @param id   - destination id
     * @throws FileNotFoundException - If there's no file
     */
    public FileSend(String path, String id) throws IOException {
        if (path.isEmpty()) {
            throw new RuntimeException("Empty File Path.");
        }
        fileToSend = new File(path);
        if (!fileToSend.exists()) {
            throw new FileNotFoundException("File\n\"" + path + "\"\nnot found.");
        }
        try {
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            sslSocket = (SSLSocket) sslSocketFactory.createSocket(SERVER_IP, SERVER_PORT);
        } catch (IOException ex) {
            throw new UnreachableHostException("Unreachable Host.");
        }
        canceled = false;
        hanshake(id);
    }

    private void hanshake(String id) throws IOException {
        new Transmission(sslSocket).sendText("SENDER");
        if (!new Transmission(sslSocket).readText().equals("RECEPTOR_ID?"))
            throw new HandshakeException("Handshake error");
        new Transmission(sslSocket).sendText(id);
        String result = new Transmission(sslSocket).readText();
        if (result.equals("NOT_A_VALID_ID")) {
            throw new HandshakeException("Selected ID not waiting for a file.");
        } else if (!result.equals("ID_IS_VALID")) {
            throw new HandshakeException("Handshake error");
        }
    }

    /**
     * Getter for status
     *
     * @return the status of the current transfer
     */
    public String getStatus() {
        return status;
    }

    public void start() {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            fis = new FileInputStream(fileToSend);
            bis = new BufferedInputStream(fis);
            os = sslSocket.getOutputStream();
            System.out.println("Sending\n" + fileToSend.getAbsolutePath() + "\n(" + fileToSend.length() + " bytes)");
            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            while (!canceled && (count = bis.read(buffer)) > 0) {
                os.write(buffer, 0, count);
            }
            os.flush();
            if (!canceled) {
                status = "Sent\n\"" + fileToSend.getAbsolutePath() + "\"\n(" + fileToSend.length() + " bytes)";
            } else {
                status = "Canceled";
            }
        } catch (IOException exception) {
            canceled = true;
            status = exception.getMessage();
        } finally {
            finish(fis, bis, os, sslSocket);
        }
    }


    /**
     * Finishes a transformation process by closing all the streams and
     * deleting the file if it was aborted
     *
     * @param closeables the streams
     */
    private void finish(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
        if (canceled) fileToSend.delete(); // If it was aborted the file might be corrupted
    }
}
