package com.comandulli.lib.download;

/**
 * The interface Download queue listener.
 */
public interface DownloadQueueListener {

    /**
     * On queue change.
     *
     * @param size the size
     */
    void onQueueChange(int size);

}
