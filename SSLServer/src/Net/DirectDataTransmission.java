package Net;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lbsilva on 25-Nov-17.
 */
public class DirectDataTransmission {

    private static final int BUFFER_SIZE = 1024 * 16;

    private SSLSocket sender;
    private SSLSocket receptor;

    public DirectDataTransmission(SSLSocket sender, SSLSocket receptor) {
        this.sender = sender;
        this.receptor = receptor;
    }

    public void start() throws IOException {
        InputStream inFromSender = sender.getInputStream();
        OutputStream outToReceptor = receptor.getOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int count;
        while ((count = inFromSender.read(buffer)) > 0) {
            outToReceptor.write(buffer, 0, count);
        }
    }
}
