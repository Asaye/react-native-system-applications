package com.sysapps.alarm;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.app.NotificationManager;
import android.app.Notification;
import android.support.v4.app.NotificationCompat;
import android.R;
import android.app.job.JobService;
import android.app.job.JobParameters;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;

public class AlarmService extends JobService {

	private int id;

	private Handler mJobHandler = new Handler( new Handler.Callback() {
	    @Override
	    public boolean handleMessage( Message msg ) {
	    	JobParameters params = (JobParameters) msg.obj;
	        displayNotif(params);
	        jobFinished(params, false);
	        return true;
	    }
	});

	@Override
	public boolean onStartJob(JobParameters params) {
		id = params.getJobId();
	    mJobHandler.sendMessage( Message.obtain( mJobHandler, id, params ) );
	    return true;
	}
	 
	@Override
	public boolean onStopJob(JobParameters params) {
		id = params.getJobId();
	    mJobHandler.removeMessages(id);
	    return false;
	}

	private void displayNotif(JobParameters params) {		      
		
      	try {
      		Context context = getApplicationContext();
      		PersistableBundle pBundle = params.getExtras();
      		String channelId = pBundle.getString("channelId");
	      	NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
				.setSmallIcon(R.mipmap.sym_def_app_icon)
				.setContentTitle(pBundle.getString("title"))
				.setContentText(pBundle.getString("content"))
				.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
				.setPriority(NotificationCompat.PRIORITY_HIGH);

			Notification notification = builder.build();

      		NotificationManager nm =
					(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.notify(id, notification);
			NotificationEvent.emitEvent(pBundle);
		} catch(Exception ex) {
      			
      	}
	}
}
