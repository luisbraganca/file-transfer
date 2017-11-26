package Net;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by lbsilva on 23-Nov-17.
 */
public final class ReceptorList {

    private static final int MAX_ID_LENGTH = 4;
    private static HashMap<String, SSLSocket> receptorsList;

    static {
        receptorsList = new HashMap<>();
        new Thread(new InactiveReceptorRemover(receptorsList)).start();
    }

    public static SSLSocket getSocket(String id) {
        return receptorsList.get(id);
    }

    /**
     * Returns Id
     */
    public static synchronized String addReceptor(SSLSocket socket) {
        String id = generateId();
        receptorsList.put(id, socket);
        return id;
    }

    public static void removeReceptor(String id) {
        try {
            receptorsList.get(id).close();
        } catch (IOException ignored) {
        }
        receptorsList.remove(id);
    }

    private static String generateId() {
        String id;
        do {
            id = randomString(MAX_ID_LENGTH);

        } while (existsId(id));
        return id;
    }

    public static boolean existsId(String id) {
        return receptorsList.containsKey(id);
    }

    private static String randomString(int length) {
        String possibleChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder();
        for (int i = 0; i < length; i++) {
            randomString.append(possibleChars.charAt(random.nextInt(possibleChars.length())));
        }
        return randomString.toString();
    }
}

