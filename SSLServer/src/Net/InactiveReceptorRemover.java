package Net;

import Util.Logger;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lbsilva on 25-Nov-17.
 */
public class InactiveReceptorRemover implements Runnable {

    private HashMap<String, SSLSocket> receptorList;

    public InactiveReceptorRemover(HashMap<String, SSLSocket> receptorList) {
        this.receptorList = receptorList;
    }

    public void sleep(float seconds) {
        try {
            Thread.sleep((int) (seconds * 1000));
        } catch (InterruptedException ignored) {
        }
    }

    private synchronized void removeInactiveReceptors() {
        for (Map.Entry<String, SSLSocket> receptor : receptorList.entrySet()) {
            try {
                if (!receptor.getValue().getInetAddress().isReachable(5000)) {
                    Logger.log(receptor.getValue().getInetAddress().getHostAddress() + " (" + receptor.getKey() + ") is inactive, aborting.");
                    receptorList.remove(receptor.getKey());
                    receptor.getValue().close();
                }
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            sleep(0.25f);
            removeInactiveReceptors();
        }
    }
}
