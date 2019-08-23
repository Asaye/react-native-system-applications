package com.sysapps.volume;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ActivityEventListener;

import android.content.Intent;
import android.content.Context;
import android.provider.ContactsContract;
import android.app.NotificationManager;
import android.app.Activity;
import android.os.Build;
import android.media.AudioManager;

import java.util.Map;
import java.util.HashMap;

public class Volume extends ReactContextBaseJavaModule {

    private static final String TAG = "VOLUME_CHANGE_ERROR";
    private static final int PERMISSION_NOTIFICATION_POLICY = 10100;
    
    private Integer mRingerMode;
    private Promise mRingerPromise;

    private AudioManager mAudioManager = (AudioManager) getReactApplicationContext()
                                .getSystemService(Context.AUDIO_SERVICE); 

    private final ActivityEventListener permissionsListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            if (requestCode == PERMISSION_NOTIFICATION_POLICY && resultCode == Activity.RESULT_OK) {
                try {
                    if (mRingerMode != null) {
                        mAudioManager.setRingerMode(mRingerMode.intValue()); 
                    } 
                } catch (Exception ex) {
                    mRingerPromise.reject(TAG, ex.getMessage()); 
                }
            } else if (requestCode == PERMISSION_NOTIFICATION_POLICY && resultCode == Activity.RESULT_CANCELED) {
               mRingerPromise.reject(TAG, "User canceled permission request.");  
            }
            mRingerPromise = null;   
        }
    };

    public Volume(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(permissionsListener);
    }

    @Override
    public String getName() {
        return "Volume";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        
        constants.put("alarm", AudioManager.STREAM_ALARM);
        constants.put("music", AudioManager.STREAM_MUSIC);
        constants.put("notification", AudioManager.STREAM_NOTIFICATION);
        constants.put("ring", AudioManager.STREAM_RING);
        constants.put("voicecall", AudioManager.STREAM_VOICE_CALL);
        constants.put("system", AudioManager.STREAM_SYSTEM);

        constants.put("normal", AudioManager.RINGER_MODE_NORMAL);
        constants.put("silent", AudioManager.RINGER_MODE_SILENT);
        constants.put("vibration", AudioManager.RINGER_MODE_VIBRATE);
      
        return constants;
    }

    @ReactMethod
    public void indexOf(String type, Promise promise) {
        try {                 
            WritableMap map = new WritableNativeMap();
            int stream = (int) getConstants().get(type);
            map.putInt("minimum", mAudioManager.getStreamMinVolume(stream));
            map.putInt("maximum", mAudioManager.getStreamMaxVolume(stream));
            map.putInt("current", mAudioManager.getStreamVolume(stream));
            promise.resolve(map);
        } catch (Exception ex) {
            promise.reject(TAG, ex.getMessage());
        }
    }

    @ReactMethod
    public void indexTo(String type, int index, Promise promise) {  
        try {  
            int stream = (int) getConstants().get(type);
            mAudioManager.setStreamVolume(stream, index, AudioManager.FLAG_SHOW_UI);
        } catch (Exception ex) {
            promise.reject(TAG, ex.getMessage());
        }
    }
    
    @ReactMethod
    public void silence(Promise promise) {
        mRingerPromise = promise;
        try { 
            setRingerMode("silent");
        } catch (Exception ex) {
            promise.reject(TAG, ex.getMessage());
        }
    }
  
    @ReactMethod
    public void normalize(Promise promise) {
        mRingerPromise = promise;
        try { 
            setRingerMode("normal");
        } catch (Exception ex) {
            promise.reject(TAG, ex.getMessage());
        }
    }

    @ReactMethod
    public void vibrate(Promise promise) {
        mRingerPromise = promise;
        try { 
            setRingerMode("vibration");
        } catch (Exception ex) {
            promise.reject(TAG, ex.getMessage());
        }
    }

    private void setRingerMode(String type) {

        Activity activity = getCurrentActivity();
        NotificationManager notificationManager = 
            (NotificationManager) activity.getSystemService(Context.NOTIFICATION_SERVICE);
        mRingerMode = (Integer) getConstants().get(type);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                                android.provider.Settings
                                .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            activity.startActivityForResult(intent, PERMISSION_NOTIFICATION_POLICY);
        } else {
            mAudioManager.setRingerMode(mRingerMode.intValue()); 
        }
    }
}
