package com.comandulli.lib.download;

/**
 * The type Job listener.
 */
public abstract class JobListener {

    private DownloadJob assignedJob;

    /**
     * Register.
     *
     * @param job the job
     */
    public void register(DownloadJob job) {
        this.assignedJob = job;
    }

    /**
     * Deregister.
     */
    public void deregister() {
        assignedJob.removeListener(this);
    }

    /**
     * Gets assigned job.
     *
     * @return the assigned job
     */
    public DownloadJob getAssignedJob() {
        return assignedJob;
    }

    /**
     * On execute.
     *
     * @param wasCancelled the was cancelled
     */
    public void onExecute(boolean wasCancelled) {
		
	}

    /**
     * On start.
     *
     * @param length the length
     */
    public void onStart(int length) {

	}

    /**
     * On progress.
     *
     * @param progress   the progress
     * @param downloaded the downloaded
     * @param length     the length
     * @param speed      the speed
     */
    public void onProgress(int progress, int downloaded, int length, float speed) {

	}

    /**
     * On finish.
     */
    public void onFinish() {

	}

    /**
     * On cancelled.
     *
     * @param hasError the has error
     */
    public void onCancelled(boolean hasError) {

	}

}
