package com.comandulli.lib.download;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * The type Cancel download receiver.
 */
public class CancelDownloadReceiver extends BroadcastReceiver {

    /**
     * On receive.
     *
     * @param context the context
     * @param intent  the intent
     */
    @Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, DownloadService.class);
		service.setAction(DownloadService.CANCEL_ACTION);
		context.startService(service);
	}

}