package com.comandulli.lib.download;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * The type Download service.
 */
public class DownloadService extends Service {

    /**
     * The constant CANCEL_ACTION.
     */
    public static final String CANCEL_ACTION = "CancelAction";
    private final IBinder mBinder = new DownloadBinder();
    private Hashtable<String, DownloadJob> jobs;
    private NotificationJobListener notificationListener;
    private final List<DownloadQueueListener> queueListeners = new ArrayList<>();

    /**
     * On start command int.
     *
     * @param intent  the intent
     * @param flags   the flags
     * @param startId the start id
     * @return the int
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (jobs == null) {
            jobs = new Hashtable<>();
        }
        for (DownloadQueueListener downloadQueueListener : queueListeners) {
            downloadQueueListener.onQueueChange(0);
        }
        if (notificationListener == null && DownloadManagerResources.testResources()) {
            notificationListener = new NotificationJobListener(this);
        }
        if (intent != null && intent.getAction() != null && intent.getAction().equals(CANCEL_ACTION)) {
            stopAllWork();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * Add job.
     *
     * @param job the job
     */
    public void addJob(DownloadJob job) {
        jobs.put(job.getName(), job);
        if(notificationListener != null) {
            job.registerNamedListener(notificationListener, "Not");
        }
        job.start();
        for (DownloadQueueListener downloadQueueListener : queueListeners) {
            downloadQueueListener.onQueueChange(jobs.size());
        }
    }

    /**
     * Gets job.
     *
     * @param name the name
     * @return the job
     */
    public DownloadJob getJob(String name) {
        return jobs.get(name);
    }

    /**
     * Gets job list.
     *
     * @return the job list
     */
    public List<DownloadJob> getJobList() {
        List<DownloadJob> jobList = new ArrayList<>();
        jobList.addAll(jobs.values());
        return jobList;
    }

    /**
     * Add queue listener.
     *
     * @param downloadQueueListener the download queue listener
     */
    public void addQueueListener(DownloadQueueListener downloadQueueListener) {
        queueListeners.add(downloadQueueListener);
    }

    /**
     * Remove queue listener.
     *
     * @param downloadQueueListener the download queue listener
     */
    public void removeQueueListener(DownloadQueueListener downloadQueueListener) {
        queueListeners.remove(downloadQueueListener);
    }

    /**
     * Remove job.
     *
     * @param name the name
     */
    public void removeJob(String name) {
        jobs.remove(name);
        if(notificationListener != null) {
            notificationListener.onRemove();
        }
        for (DownloadQueueListener downloadQueueListener : queueListeners) {
            downloadQueueListener.onQueueChange(jobs.size());
        }
    }

    /**
     * On destroy.
     */
    @Override
    public void onDestroy() {
        stopAllWork();
    }

    /**
     * Stop all work.
     */
    public void stopAllWork() {
        for (DownloadJob job : jobs.values()) {
            job.cancel();
        }
        jobs.clear();
        if(notificationListener != null) {
            notificationListener.end();
        }
        for (DownloadQueueListener downloadQueueListener : queueListeners) {
            downloadQueueListener.onQueueChange(0);
        }
    }

    /**
     * On bind binder.
     *
     * @param intent the intent
     * @return the binder
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * The type Download binder.
     */
    public class DownloadBinder extends Binder {
        /**
         * Gets service.
         *
         * @return the service
         */
        DownloadService getService() {
            return DownloadService.this;
        }
    }

}