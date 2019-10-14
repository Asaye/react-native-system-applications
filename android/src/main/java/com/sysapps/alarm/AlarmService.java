package com.sysapps.alarm;

import com.sysapps.utils.AppState;

import com.facebook.react.ReactApplication;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.Arguments;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.app.NotificationManager;
import android.app.Notification;
import android.app.Activity;
import android.support.v4.app.NotificationCompat;
import android.R;
import android.app.job.JobService;
import android.app.job.JobParameters;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Bundle;
import org.json.JSONObject;

public class AlarmService extends JobService {

	private int id;

	private Handler mJobHandler = new Handler( new Handler.Callback() {
	    @Override
	    public boolean handleMessage( Message msg ) {
	    	JobParameters params = (JobParameters) msg.obj;    	

	    	try {
	    		displayNotif(params);
	    		Context context = getApplicationContext();
	    		if (AppState.isAppInForeground(context)) {
	      			emitEvent(params);
	    		} else {				   
			       Intent serviceIntent = new Intent(
			          context,
			          EventEmitter.class
			       );
			       serviceIntent.putExtra("response", new Bundle(params.getExtras()));      
	      		   context.startService(serviceIntent);
	      		   HeadlessJsTaskService.acquireWakeLockNow(context);
	    		}
	    	} catch (Exception e) {
	    	} finally {
	    		jobFinished(params, false);
	    	}
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
		} catch(Exception ex) {
      	}
	}

	public void emitEvent(JobParameters params) {
		Context context = getApplicationContext();
		ReactNativeHost host = ((ReactApplication)context).getReactNativeHost();
		PersistableBundle pBundle = params.getExtras();
		ReactInstanceManager manager = null;
		ReactApplicationContext reactContext = null;		
        String response;

		if (host != null) {
			manager = host.getReactInstanceManager();
			reactContext = (ReactApplicationContext) manager.getCurrentReactContext();	
		}             
        
        // try {
        //     JSONObject json = new JSONObject();
        //     for (String key: pBundle.keySet()) {
        //         if (pBundle.getString(key) != null) {
        //             json.put(key, pBundle.getString(key)); 
        //         } else if (pBundle.get(key) instanceof Double) {
        //             json.put(key, pBundle.getDouble(key)); 
        //         } else if (pBundle.get(key) instanceof Boolean) {
        //             json.put(key, pBundle.getBoolean(key)); 
        //         }                               
        //     } 
        //     response = json.toString();
        // } catch (Exception ex){
        //     response = ex.getMessage();
        // }
        Bundle bundle = new Bundle(pBundle);
        //Arguments.makeNativeMap(bundle)
        if (reactContext != null) {
        	reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit("onNotification", Arguments.makeNativeMap(bundle)); 
        }                    
    }
}
