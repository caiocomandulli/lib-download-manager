package com.comandulli.lib.download;

import android.content.Context;
import android.os.AsyncTask.Status;

import java.util.HashMap;

/**
 * The type Download job.
 */
public class DownloadJob {

	private final String name;
    /**
     * The Path.
     */
    protected final String path;
    /**
     * The Url.
     */
    protected final String url;
	private Object referringData;
    /**
     * The Listeners.
     */
    protected final HashMap<String, JobListener> listeners;
	private DownloadTask task;
    /**
     * The Is running.
     */
    protected boolean isRunning;
    /**
     * The Is done.
     */
    protected boolean isDone;
    /**
     * The Is cancelled.
     */
    protected boolean isCancelled;
    /**
     * The Has error.
     */
    protected boolean hasError;
    /**
     * The Progress.
     */
    protected int progress = DownloadTask.INDETERMINATE_PROGRESS;
    /**
     * The Speed.
     */
    protected float speed;
	
	private final Context context;

    /**
     * Instantiates a new Download job.
     *
     * @param name    the name
     * @param url     the url
     * @param path    the path
     * @param context the context
     */
    public DownloadJob(String name, String url, String path, Context context) {
		this.context = context;
		this.name = name;
		this.path = path;
		this.url = url;
        this.listeners = new HashMap<>();
		this.task = new DownloadTask(this);
	}

    /**
     * Sets referring data.
     *
     * @param referringData the referring data
     */
    public void setReferringData(Object referringData) {
		this.referringData = referringData;
	}

    /**
     * Gets referring data.
     *
     * @return the referring data
     */
    public Object getReferringData() {
		return referringData;
	}

    /**
     * Is running boolean.
     *
     * @return the boolean
     */
    public boolean isRunning() {
		return isRunning;
	}

    /**
     * Is done boolean.
     *
     * @return the boolean
     */
    public boolean isDone() {
		return isDone;
	}

    /**
     * Is cancelled boolean.
     *
     * @return the boolean
     */
    public boolean isCancelled() {
		return isCancelled;
	}

    /**
     * Has error boolean.
     *
     * @return the boolean
     */
    public boolean hasError() {
		return hasError;
	}

    /**
     * Gets progress.
     *
     * @return the progress
     */
    public int getProgress() {
		return progress;
	}

    /**
     * Gets speed.
     *
     * @return the speed
     */
    public float getSpeed() {
		return speed;
	}

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
		return name;
	}

    /**
     * Gets path.
     *
     * @return the path
     */
    public String getPath() {
		return path;
	}

    /**
     * Gets context.
     *
     * @return the context
     */
    public Context getContext() {
		return context;
	}

    /**
     * Start.
     */
    public void start() {
		if (task.isCancelled()) {
			task = new DownloadTask(this);
		}
		if (task.getStatus() != Status.RUNNING) {
			task.execute("");
		}
        for (JobListener jobListener : listeners.values()) {
			jobListener.onExecute(isCancelled);
		}
		hasError = false;
		isCancelled = false;
		isDone = false;
	}

    /**
     * Cancel.
     */
    public void cancel() {
		isCancelled = true;
		task.cancel(true);
	}

    /**
     * Register listener.
     *
     * @param listener the listener
     */
    public void registerListener(JobListener listener) {
        registerNamedListener(listener, String.valueOf(System.currentTimeMillis()));
	}

    /**
     * Register named listener.
     *
     * @param listener the listener
     * @param name     the name
     */
    public void registerNamedListener(JobListener listener, String name) {
        listener.register(this);
        listeners.put(name, listener);
    }

    /**
     * Remove named listener.
     *
     * @param name the name
     */
    public void removeNamedListener(String name) {
        listeners.remove(name);
    }

    /**
     * Remove listener.
     *
     * @param listener the listener
     */
    public void removeListener(JobListener listener) {
        listeners.remove(listener);
	}
	
}
