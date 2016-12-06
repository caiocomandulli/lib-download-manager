package com.comandulli.lib.download;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.comandulli.lib.download.DownloadService.DownloadBinder;

/**
 * The type Download manager.
 */
public class DownloadManager {

    /**
     * The enum Download type.
     */
    public enum DownloadType {
        /**
         * Not set download type.
         */
        NotSet, /**
         * Mobile download type.
         */
        Mobile, /**
         * Wi fi only download type.
         */
        WiFiOnly
	}

	private static DownloadType currentType = DownloadType.NotSet;
	private static DownloadService service;
	private static final ServiceConnection serviceConnection = new ServiceConnection() {

		@Override
        public void onServiceConnected(ComponentName name, IBinder service) {
			DownloadBinder b = (DownloadBinder) service;
			DownloadManager.service = b.getService();
		}

		@Override
        public void onServiceDisconnected(ComponentName name) {
			service = null;
		}
	};

    /**
     * Bind service.
     *
     * @param context the context
     */
    public static void bindService(Context context) {
		Intent intent = new Intent(context, DownloadService.class);
		context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

    /**
     * Unbind service.
     *
     * @param context the context
     */
    public static void unbindService(Context context) {
		context.unbindService(serviceConnection);
	}

    /**
     * Add job.
     *
     * @param job the job
     */
    public static void addJob(DownloadJob job) {
		if (service != null) {
			service.addJob(job);
		}
	}

    /**
     * Remove job.
     *
     * @param job the job
     */
    public static void removeJob(DownloadJob job) {
		if (service != null) {
			service.removeJob(job.getName());
		}
	}

    /**
     * Remove job.
     *
     * @param name the name
     */
    public static void removeJob(String name) {
		if (service != null) {
			service.removeJob(name);
		}
	}

    /**
     * Gets job.
     *
     * @param name the name
     * @return the job
     */
    public static DownloadJob getJob(String name) {
		if (service != null) {
			return service.getJob(name);
		} else {
			return null;
		}
	}

    /**
     * Add queue listener.
     *
     * @param downloadQueueListener the download queue listener
     */
    public static void addQueueListener(DownloadQueueListener downloadQueueListener) {
		if (service != null) {
			service.addQueueListener(downloadQueueListener);
		}
	}

    /**
     * Remove queue listener.
     *
     * @param downloadQueueListener the download queue listener
     */
    public static void removeQueueListener(DownloadQueueListener downloadQueueListener) {
		if (service != null) {
			service.removeQueueListener(downloadQueueListener);
		}
	}

    /**
     * Gets job list.
     *
     * @return the job list
     */
    public static List<DownloadJob> getJobList() {
		if (service != null) {
			return service.getJobList();
		} else {
			return null;
		}
	}

    /**
     * Gets type.
     *
     * @return the type
     */
    public static DownloadType getType() {
		return currentType;
	}

    /**
     * Sets type.
     *
     * @param type the type
     */
    public static void setType(DownloadType type) {
		currentType = type;
	}

}
