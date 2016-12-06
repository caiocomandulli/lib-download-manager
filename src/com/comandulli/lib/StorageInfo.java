package com.comandulli.lib;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Storage info utils.
 */
public class StorageInfo {

    /**
     * The constant MEGA_TO_BYTE.
     */
    public static final long MEGA_TO_BYTE = 1000000L;

    /**
     * Amount of free memory.
     *
     * @return the memory
     */
    @SuppressWarnings("deprecation")
    public static long freeMemory() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        return (long) statFs.getAvailableBlocks() * (long) statFs.getBlockSize();
    }

    /**
     * Amount of total memory.
     *
     * @return the memory
     */
    @SuppressWarnings("deprecation")
    public static long totalMemory() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        return (long) statFs.getBlockCount() * (long) statFs.getBlockSize();
    }

    /**
     * Gets size of a file.
     *
     * @param file the file
     * @return the file size
     */
    public static long getFileSize(final File file) {
        if (file == null || !file.exists()) {
            return 0;
        }
        if (!file.isDirectory()) {
            return file.length();
        }
        final List<File> dirs = new LinkedList<File>();
        dirs.add(file);
        long result = 0;
        while (!dirs.isEmpty()) {
            final File dir = dirs.remove(0);
            if (!dir.exists()) {
                continue;
            }
            final File[] listFiles = dir.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                continue;
            }
            for (final File child : listFiles) {
                result += child.length();
                if (child.isDirectory()) {
                    dirs.add(child);
                }
            }
        }
        return result;
    }

    /**
     * Human readable byte count.
     *
     * @param bytes the bytes
     * @param si    the si
     * @return the human readable string.
     */
    public static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) {
            return bytes + " B";
        }
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format((Locale) null, "%.1f%sB", bytes / Math.pow(unit, exp), pre);
    }

}
