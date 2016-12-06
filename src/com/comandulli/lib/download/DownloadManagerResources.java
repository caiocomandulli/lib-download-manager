package com.comandulli.lib.download;

/**
 * Created by Caio on 08-Nov-16.
 */
public class DownloadManagerResources {

    /**
     * The constant smallIconResource.
     */
    public static int smallIconResource;
    /**
     * The constant downloadIconResource.
     */
    public static int downloadIconResource;
    /**
     * The constant completeIconResource.
     */
    public static int completeIconResource;
    /**
     * The constant colorResource.
     */
    public static int colorResource;
    /**
     * The constant downloadTitleString.
     */
    public static int downloadTitleString;
    /**
     * The constant cancelAllString.
     */
    public static int cancelAllString;
    /**
     * The constant cancelledString.
     */
    public static int cancelledString;
    /**
     * The constant downloadedString.
     */
    public static int downloadedString;
    /**
     * The constant downloadingString.
     */
    public static int downloadingString;
    /**
     * The constant connectingString.
     */
    public static int connectingString;
    /**
     * The Activity.
     */
    public static Class<?> activity;

    /**
     * Test resources boolean.
     *
     * @return the boolean
     */
    public static boolean testResources() {
        return smallIconResource != 0 && downloadIconResource != 0 && completeIconResource != 0 && colorResource != 0 && downloadTitleString != 0 && cancelAllString != 0 && cancelledString != 0 && downloadedString != 0 && downloadingString != 0 && connectingString != 0 && activity != null;
    }

    /**
     * Sets small icon.
     *
     * @param smallIcon the small icon
     */
    public static void setSmallIcon(int smallIcon) {
        smallIconResource = smallIcon;
    }

    /**
     * Sets download icon.
     *
     * @param downloadIcon the download icon
     */
    public static void setDownloadIcon(int downloadIcon) {
        downloadIconResource = downloadIcon;
    }

    /**
     * Sets complete icon.
     *
     * @param completeIcon the complete icon
     */
    public static void setCompleteIcon(int completeIcon) {
        completeIconResource = completeIcon;
    }

    /**
     * Sets color.
     *
     * @param color the color
     */
    public static void setColor(int color) {
        colorResource = color;
    }

    /**
     * Sets download title.
     *
     * @param downloadTitle the download title
     */
    public static void setDownloadTitle(int downloadTitle) {
        downloadTitleString = downloadTitle;
    }

    /**
     * Sets cancel all.
     *
     * @param cancelAll the cancel all
     */
    public static void setCancelAll(int cancelAll) {
        cancelAllString = cancelAll;
    }

    /**
     * Sets cancelled.
     *
     * @param cancelled the cancelled
     */
    public static void setCancelled(int cancelled) {
        cancelledString = cancelled;
    }

    /**
     * Sets downloaded.
     *
     * @param downloaded the downloaded
     */
    public static void setDownloaded(int downloaded) {
        downloadedString = downloaded;
    }

    /**
     * Sets downloading.
     *
     * @param downloading the downloading
     */
    public static void setDownloading(int downloading) {
        downloadingString = downloading;
    }

    /**
     * Sets connecting.
     *
     * @param connecting the connecting
     */
    public static void setConnecting(int connecting) {
        connectingString = connecting;
    }

    /**
     * Sets intent receiver.
     *
     * @param activity the activity
     */
    public static void setIntentReceiver(Class<?> intentReceiver) {
        activity = intentReceiver;
    }

}
