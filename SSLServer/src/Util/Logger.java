package Util;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lbsilva on 03-Nov-17.
 */
public final class Logger {

    private static final String LOG_FILE_EXTENSION = ".log";

    private Logger() {
    }

    public static boolean log(String message, boolean echo) {
        String content = getTime() + " " + message;
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(getDate() + LOG_FILE_EXTENSION), true)));
            pw.println(content);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            return false;
        }
        if (echo) {
            System.out.println(content);
        }
        return true;
    }

    public static boolean log(String message) {
        return log(message, true);
    }

    private static String getDate() {
        return getDateTime("yyyy_MM_dd");
    }

    private static String getTime() {
        return getDateTime("HH:mm:ss");
    }

    private static String getDateTime(String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
    }
}
