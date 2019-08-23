package com.sysapps.alarm;

import com.sysapps.utils.Storage;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReadableMap;

import android.content.Context;
import android.app.Activity;
import android.R;
import android.app.job.JobScheduler;
import android.app.job.JobInfo;
import android.content.ComponentName;
import android.os.PersistableBundle;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.io.File;

public class Alarm extends ReactContextBaseJavaModule {

    private static final String TAG = "ALARM_ERROR";
   
    private JobScheduler mJobScheduler;

    public Alarm(ReactApplicationContext reactContext) {
        super(reactContext);  
        mJobScheduler = (JobScheduler) reactContext.getSystemService( Context.JOB_SCHEDULER_SERVICE );    
    }

    @Override
    public String getName() {
        return "Alarm";
    }

    @ReactMethod
    public void schedule(ReadableMap map, Promise promise) {
        validate(map, promise);
        String id = map.getString("channelId");
        long time = (long) map.getDouble("time");        
               
        Date now = new Date();
        Date future = new Date(time);
        long delay = future.getTime() - now.getTime();
        PersistableBundle pBundle = updateBundle(new PersistableBundle(), map);

        scheduleJob(pBundle, id, delay);
    }

    @ReactMethod
    public void update(ReadableMap map, Promise promise) {
        validate(map, promise);
        String id = map.getString("channelId");        
        PersistableBundle pBundle = updateBundle(getJobExtras(id), map);

        cancelJob(id);

        Date now = new Date();
        Date future = new Date((long) map.getDouble("time"));
        long delay = future.getTime() - now.getTime();
        
        if (pBundle != null) {
            scheduleJob(pBundle, pBundle.getString("channelId"), delay);   
        } else {
            promise.reject(TAG, "No data with specified channelId could be retrieved");
        }        
    }

    @ReactMethod
    public void cancel(String id, Promise promise) {
        if (id == null) return;
        cancelJob(id);
    }

    @ReactMethod
    public void cancelAll(Promise promise) {
        try {
            mJobScheduler.cancelAll();
            File[] files = getReactApplicationContext().getFilesDir().listFiles();
            if (files != null){
                for (File file: files){
                    file.delete();
                }
            }
        }catch (Exception ex) {

        }
    }

    @ReactMethod
    public void refer(String id, Promise promise) { 
        if (id == null) return;      
        try {
            PersistableBundle pBundle = getJobExtras(id);
            JSONObject json = new JSONObject();
            for (String key: pBundle.keySet()) {
                if (pBundle.getString(key) != null) {
                    json.put(key, pBundle.getString(key)); 
                } else if (pBundle.get(key) instanceof Double) {
                    json.put(key, pBundle.getDouble(key)); 
                } else if (pBundle.get(key) instanceof Boolean) {
                    json.put(key, pBundle.getBoolean(key)); 
                }                               
            }            
            promise.resolve(json.toString());
        } catch (Exception ex) {
            promise.reject(TAG, ex.getMessage());
        }
    }

    private void validate(ReadableMap map, Promise promise) {
        if (map == null) {
            promise.reject(TAG, "Request parameter cannot be null.");            
        }

        if (map.getString("channelId") == null) {
            promise.reject(TAG, "Request parameter should contain 'id' property.");
        }
        
        if (map.getDouble("time") <= 0) {
            promise.reject(TAG, "Request parameter should contain 'time' property.");
        }
    }

    private PersistableBundle updateBundle(PersistableBundle pBundle, ReadableMap map) {
        
        if (pBundle == null) {
            return null;
        }

        Map data = map.toHashMap();
        Set<String> keys = data.keySet();

        for (String item: keys) {
            if (data.get(item) instanceof Double) {
                pBundle.putDouble((String) item, (Double) data.get(item));
            } else if (data.get(item) instanceof Boolean) {
                pBundle.putBoolean((String) item, (Boolean) data.get(item));
            } else {
                pBundle.putString((String) item, (String) data.get(item));
            }
        }        

        return pBundle;
    }

    private void scheduleJob(PersistableBundle pBundle, String id, long delay) {
        if (id == null) return;
        try {
            Activity activity = getCurrentActivity();
            Context context = getReactApplicationContext();
            int num =(int) (Math.random()*10000000); 
            JobInfo.Builder builder = new JobInfo.Builder(num,
                    new ComponentName( activity.getPackageName(), AlarmService.class.getName())); 
            builder.setExtras(pBundle);
            builder.setPersisted(true);
            builder.setMinimumLatency(delay); 
            JobInfo jobInfo = builder.build();         
            int jobId = mJobScheduler.schedule(jobInfo);       
            Storage.writeObject(context, id, new Integer(num));
        } catch (Exception ex) {

        }
    }

    private void cancelJob(String id) {
        if (id == null) return;
        try {
            Context context = getReactApplicationContext();
            Integer jobId = (Integer) Storage.readObject(context, id);
            mJobScheduler.cancel(jobId.intValue());
            context.deleteFile(id);
        } catch (Exception ex) {

        }
    }

    private PersistableBundle getJobExtras(String id) {
        if (id == null) {
            return null;
        }

        try {
            Context context = getReactApplicationContext();
            Integer jobId = (Integer) Storage.readObject(context, id);
            JobInfo jobInfo = mJobScheduler.getPendingJob(jobId.intValue());
            return jobInfo.getExtras();
        } catch (Exception ex) {
            
        }
        return null;
    }
}
 