package com.comandulli.lib.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.comandulli.lib.download.DownloadManager.DownloadType;

/**
 * The type Download task.
 */
public class DownloadTask extends AsyncTask<String, Integer, String> {

    /**
     * The constant INDETERMINATE_PROGRESS.
     */
    public static final int INDETERMINATE_PROGRESS = -1;

	private final DownloadJob job;
	private File file;

    /**
     * Instantiates a new Download task.
     *
     * @param job the job
     */
    public DownloadTask(DownloadJob job) {
		this.job = job;
	}

    /**
     * On cancelled.
     */
    @Override
	protected void onCancelled() {
        job.hasError = !job.isCancelled;
		if (file != null) {
            if(!file.delete()) {
                Log.w("File deletion", "Failed to delete file");
            }
		}
		job.isCancelled = true;
		job.isRunning = false;
		job.progress = INDETERMINATE_PROGRESS;
		Collection<JobListener> collection = job.listeners.values();
	        for (JobListener jobListener : collection) {
			jobListener.onCancelled(job.hasError);
		}
	}

    /**
     * On cancelled.
     *
     * @param result the result
     */
    @Override
    protected void onCancelled(String result) {
		onCancelled();
	}

    /**
     * Do in background string.
     *
     * @param params the params
     * @return the string
     */
    @Override
	protected synchronized String doInBackground(String... params) {
		int count;
		try {
			if (isCancelled()) {
				return null;
			}
			job.hasError = false;
			job.isCancelled = false;
			job.isDone = false;
			URL url = new URL(job.url);
			URLConnection connection = url.openConnection();
			connection.connect();

			if (isCancelled()) {
				return null;
			}
			int lengthOfFile = connection.getContentLength();
			InputStream input = new BufferedInputStream(url.openStream());
			file = new File(job.path + ".tmp");
			if (!file.exists()) {
				if(!file.getParentFile().mkdirs()) {
                    Log.w("Folder creation", "Failed to create folder");
                }
                if(!file.createNewFile()) {
                    Log.w("File creation", "Failed to create file");
                }
			} else {
				if(!file.delete()) {
                    Log.w("File deletion", "Failed to delete file");
                }
				if(!file.createNewFile()) {
                    Log.w("File creation", "Failed to create file");
                }
			}
			OutputStream output = new FileOutputStream(file);

			byte data[] = new byte[1024];

			long total = 0;

			if (!isCancelled()) {
				Collection<JobListener> collection = job.listeners.values();
	        		for (JobListener jobListener : collection) {
					jobListener.onStart(lengthOfFile);
				}
				job.isRunning = true;
				NotificationJobListener.currentJob = job;
			}

			long lastTotal = total;
			long lastTime = 0;
			ConnectivityManager connectivityManager = (ConnectivityManager) job.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			boolean wasWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
			publishProgress(0, 0, lengthOfFile);
			while (true) {
				// is cancelled
				if (isCancelled()) {
					break;
				}
				// check connection status and type
				if (connectivityManager.getActiveNetworkInfo() == null) {
					continue;
				} else {
					NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
					if (!wifi.isConnected()) {
						if (DownloadManager.getType() != DownloadType.Mobile) {
							continue;
						} else {
							if (wasWifi) {
								wait(1000);
								wasWifi = false;
								continue;
							}
						}
					} else {
						if (!wasWifi) {
							wait(1000);
							wasWifi = true;
							continue;
						}
					}
				}
				// read input
				count = input.read(data);
				// check if not end of input
				if (count == -1) {
					break;
				}
				total += count;
				// determine speed
				long time = new Date().getTime();
				float deltaTime = (time - lastTime) / 1000.0f;
				if (deltaTime > 1.0f) {
					long delta = total - lastTotal;
					job.speed = delta / deltaTime;
					lastTotal = total;
					lastTime = time;
				}
				// publish progress
				publishProgress((int) (total * 100 / lengthOfFile), (int) total, lengthOfFile);
				job.downloaded = (int) total;
				job.length = lengthOfFile;
				output.write(data, 0, count);
			}
			output.flush();
			output.close();
			input.close();
			if (!isCancelled()) {
				if(!file.renameTo(new File(job.path))) {
                    Log.w("File rename", "Failed to rename file");
                }
			}
		} catch (Exception e) {
			Log.w("DOWNLOAD", e);
            //noinspection WrongThread
            onCancelled();
		}
		return null;
	}

    /**
     * On progress update.
     *
     * @param values the values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
		job.progress = values[0];
		Collection<JobListener> collection = job.listeners.values();
	        for (JobListener jobListener : collection) {
			jobListener.onProgress(values[0], values[1], values[2], job.speed);
		}
	}

    /**
     * On post execute.
     *
     * @param result the result
     */
    @Override
	protected void onPostExecute(String result) {
		if (!job.isCancelled && !job.hasError) {
			Collection<JobListener> collection = job.listeners.values();
	        	for (JobListener jobListener : collection) {
				jobListener.onFinish();
			}
		}
		job.progress = INDETERMINATE_PROGRESS;
		job.isRunning = false;
		job.isDone = true;
	}
}
