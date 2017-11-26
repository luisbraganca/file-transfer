/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

import java.io.*;

/**
 * @author lbsilva
 */
public class FileSend extends FileTransmission {

    /**
     * Sends a file to a certain Id
     *
     * @param path - Path of the file to be sent
     * @param id   - destination id
     * @throws IOException - Some network failure
     */
    public FileSend(String path, String id) throws IOException {
        super(path);
        if (!file.exists()) {
            throw new FileNotFoundException("File\n\"" + path + "\"\nnot found.");
        }
        handshake(id);
    }

    /**
     * Handshake process with the server
     *
     * @param id - the id
     * @throws IOException - Some network failure
     */
    private void handshake(String id) throws IOException {
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

    @Override
    public void start() {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            os = sslSocket.getOutputStream();
            int count;
            byte[] buffer = new byte[BUFFER_SIZE];
            while (!canceled && (count = bis.read(buffer)) > 0) {
                os.write(buffer, 0, count);
            }
            os.flush(); // It's a bit redundant but just in case
            status = canceled ? "Canceled" : "Sent\n\"" + file.getAbsolutePath() + "\"\n(" + file.length() + " bytes)";
        } catch (IOException exception) {
            canceled = true;
            status = "Either canceled or some error occurred.";
        } finally {
            finish(fis, bis, os, sslSocket);
        }
    }
}
