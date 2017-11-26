package util;

import java.io.File;

/**
 *
 * @author lbsilva
 */
public class LastVisitedDirectory {

    private static final LastVisitedDirectory instance = new LastVisitedDirectory();
    private boolean changed;
    private String path;

    private LastVisitedDirectory() {
        changed = false;
    }

    public void setLastVisitedDirectory(String path) {
        this.path = path.substring(0, path.lastIndexOf(File.separator));
        if (!changed) {
            changed = true;
        }
    }

    public static LastVisitedDirectory getInstance() {
        return instance;
    }

    public boolean wasVisited() {
        return changed;
    }

    public String getPath() {
        return path;
    }
}
