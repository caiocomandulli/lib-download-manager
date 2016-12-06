package com.comandulli.lib.download;

import com.comandulli.lib.ImageFormatter;
import com.comandulli.lib.StorageInfo;
import com.comandulli.lib.download.DownloadManager.DownloadType;

import android.R.drawable;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;

/**
 * The type Notification job listener.
 */
public class NotificationJobListener extends JobListener {

	private final Context context;
    /**
     * The constant DOWNLOAD_NOTIFICATION_ID.
     */
    public static final int DOWNLOAD_NOTIFICATION_ID = 1;
    /**
     * The constant COMPLETE_NOTIFICATION_ID.
     */
    public static final int COMPLETE_NOTIFICATION_ID = 2;
    /**
     * The constant INDETERMINATE_PROGRESS.
     */
    public static final int INDETERMINATE_PROGRESS = -1;
    /**
     * The constant UPDATE_INTERVAL.
     */
    public static final long UPDATE_INTERVAL = 100;
	private final NotificationManager notificationManager;
	private final Builder downloadNotificationBuilder;
	private final Builder completeNotificationBuilder;
	private int progress = INDETERMINATE_PROGRESS;
	private float speed;
	private int running;
	private int completed;
	private int cancelled;
	private int removed;
	private boolean ended = true;

    /**
     * The constant currentJob.
     */
    public static DownloadJob currentJob;
	private final Handler handler;

    private final int cancelledString;
    private final int downloadedString;
    private final int downloadingString;
    private final int connectingString;

    /**
     * Instantiates a new Notification job listener.
     *
     * @param context the context
     */
    public NotificationJobListener(Context context) {
		this.context = context;
		this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        this.cancelledString = DownloadManagerResources.cancelledString;
        this.connectingString = DownloadManagerResources.connectingString;
        this.downloadingString = DownloadManagerResources.downloadingString;
        this.downloadedString = DownloadManagerResources.downloadedString;
		// download notification
		downloadNotificationBuilder = new Builder(context);
		downloadNotificationBuilder.setSmallIcon(DownloadManagerResources.smallIconResource);
		downloadNotificationBuilder.setOngoing(true);
		downloadNotificationBuilder.setAutoCancel(false);
		downloadNotificationBuilder.setLargeIcon(ImageFormatter.tintBitmap(context, BitmapFactory.decodeResource(context.getResources(), DownloadManagerResources.downloadIconResource), DownloadManagerResources.colorResource));
		// complete notification
		completeNotificationBuilder = new Builder(context);
		completeNotificationBuilder.setSmallIcon(DownloadManagerResources.smallIconResource);
		completeNotificationBuilder.setLargeIcon(ImageFormatter.tintBitmap(context, BitmapFactory.decodeResource(context.getResources(), DownloadManagerResources.completeIconResource), DownloadManagerResources.colorResource));
		completeNotificationBuilder.setContentTitle(context.getString(DownloadManagerResources.downloadTitleString));
		// activity intent
		Intent intent = new Intent(context, DownloadManagerResources.activity);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		stackBuilder.addNextIntent(intent);
		PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		downloadNotificationBuilder.setContentIntent(pendingIntent);
		completeNotificationBuilder.setContentIntent(pendingIntent);
		// cancel intent
		Intent deleteIntent = new Intent(context, CancelDownloadReceiver.class);
		PendingIntent cancelIntent = PendingIntent.getBroadcast(context, 0, deleteIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		downloadNotificationBuilder.addAction(drawable.ic_menu_close_clear_cancel, context.getString(DownloadManagerResources.cancelAllString), cancelIntent);
		// updater
		handler = new Handler();
        Runnable updater = new Runnable() {
            @Override
            public void run() {
                update();
                handler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
        handler.postDelayed(updater, UPDATE_INTERVAL);
	}

    /**
     * Update.
     */
    public void update() {
		if (currentJob != null) {
			this.progress = currentJob.getProgress();
			this.speed = currentJob.getSpeed();
			updateContent();
		}
	}

    /**
     * On execute.
     *
     * @param wasCancelled the was cancelled
     */
    @Override
	public void onExecute(boolean wasCancelled) {
		ended = false;
		running++;
		if (wasCancelled && cancelled > 0) {
			cancelled--;
		}
		updateContent();
	}

    /**
     * On cancelled.
     *
     * @param hasError the has error
     */
    @Override
	public void onCancelled(boolean hasError) {
		progress = INDETERMINATE_PROGRESS;
		running--;
		cancelled++;
		currentJob = null;
		updateContent();
		if (running < 1) {
			displayComplete();
			end();
		}
	}

    /**
     * On finish.
     */
    @Override
	public void onFinish() {
		progress = INDETERMINATE_PROGRESS;
		running--;
		completed++;
		currentJob = null;
		updateContent();
		if (running < 1) {
			displayComplete();
			end();
		}
	}

    /**
     * On remove.
     */
    public void onRemove() {
		if (!ended) {
			removed++;
		}
	}

    /**
     * Display complete.
     */
    public void displayComplete() {
		String cancelledString = cancelled > 0 ? "(" + cancelled + " " + context.getString(this.cancelledString) + ")" : "";
		completeNotificationBuilder.setContentText(completed + " " + context.getString(downloadedString) + "." + cancelledString);
		notificationManager.notify(COMPLETE_NOTIFICATION_ID, completeNotificationBuilder.build());
	}

    /**
     * Update content.
     */
    public void updateContent() {
		String progressString;
		if (progress != INDETERMINATE_PROGRESS) {
			progressString = "[" + progress + "%] - " + StorageInfo.humanReadableByteCount((long) speed, true) + "/s";
			downloadNotificationBuilder.setProgress(100, progress, false);
		} else {
			progressString = context.getString(connectingString);
			downloadNotificationBuilder.setProgress(0, 0, true);
		}
		int total = completed + cancelled + running - removed;
		downloadNotificationBuilder.setContentTitle(context.getString(downloadingString) + " (" + completed + "/" + total + ")");
		downloadNotificationBuilder.setContentText(progressString);
		notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, downloadNotificationBuilder.build());
	}

    /**
     * End.
     */
    public void end() {
		DownloadManager.setType(DownloadType.NotSet);
		currentJob = null;
		ended = true;
		running = 0;
		completed = 0;
		cancelled = 0;
		removed = 0;
		notificationManager.cancel(DOWNLOAD_NOTIFICATION_ID);
	}

}
