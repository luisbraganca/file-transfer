package Net;

import javax.net.ssl.SSLSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by lbsilva on 23-Nov-17.
 */
public class Transmission {
    private SSLSocket sslSocket;

    public Transmission(SSLSocket sslSocket) {
        this.sslSocket = sslSocket;
    }

    public String readText() throws IOException {
        DataInputStream input = new DataInputStream(this.sslSocket.getInputStream());
        String text = input.readUTF();
        return text;
    }

    public void sendText(String text) throws IOException {
        DataOutputStream output = new DataOutputStream(sslSocket.getOutputStream());
        output.writeUTF(text);
        output.flush();
    }

    public String readText(int timeout) throws IOException {
        sslSocket.setSoTimeout(timeout * 1000);
        return readText();
    }

    public void sendText(String text, int timeout) throws IOException {
        sslSocket.setSoTimeout(timeout * 1000);
        sendText(text);
    }
}
