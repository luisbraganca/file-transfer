package net;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * Created by lbsilva on 26-Nov-17.
 */
public abstract class FileTransmission {

    /**
     * Server IP
     */
    protected final static String SERVER_IP = "REPLACE.WITH.SERVER.IP";

    /**
     * Server port
     */
    protected final static int SERVER_PORT = 6000;

    /**
     * Buffer size for file transfers
     */
    protected final static int BUFFER_SIZE = 1024 * 16;

    /**
     * File to be sent/received
     */
    protected File file;

    /**
     * Current status of file transfer
     */
    protected String status;

    /**
     * If the file transmission was canceled
     */
    protected boolean canceled;

    /**
     * The sslSocket to be used
     */
    protected SSLSocket sslSocket;

    /**
     * Helper for file transmission operations
     * @param path - Path of the file to be sent/received
     */
    public FileTransmission (String path) {
        if (path.isEmpty()) {
            throw new IllegalArgumentException("Empty file path");
        }
        this.file = new File(path);
        canceled = false;
        try {
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            sslSocket = (SSLSocket) sslSocketFactory.createSocket(SERVER_IP, SERVER_PORT);
        } catch (IOException ex) {
            if (!canceled) throw new UnreachableHostException("Unreachable Host.");
        }
    }

    /**
     * Tries to safely close the connection, not crashing the app
     * if it doesn't succeed in closing the connection safely
     */
    public void closeConnection() {
        canceled = true;
        try {
            sslSocket.close();
        } catch (Exception ignored) {
        }
    }

    /**
     * Getter for status
     * @return the status of the current transfer
     */
    public String getStatus() {
        return status;
    }

    /**
     * Finishes a transformation process by closing all the streams
     * @param closeables the streams
     */
    protected void finish(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Starts the process
     */
    public abstract void start();
}
