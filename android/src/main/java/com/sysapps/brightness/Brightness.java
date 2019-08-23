package com.sysapps.brightness;

import com.sysapps.utils.Messages;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ActivityEventListener;

import android.app.Activity;
import android.view.WindowManager;
import android.content.Intent; 
import android.provider.Settings;
import android.net.Uri;

public class Brightness extends ReactContextBaseJavaModule {

    private static final String TAG = "BRIGHTNESS_ERROR";
    private static final int PERMISSION_REQUEST_CODE = 10020;

    private int mBrightness = 50;
    private Promise mBrightnessPromise;

    private final ActivityEventListener permissionsListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            if (requestCode == PERMISSION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                try {
                    setBrightness();  
                } catch (Exception ex) {
                    if (mBrightnessPromise != null) {
                        mBrightnessPromise.reject(TAG, ex.getMessage());
                    }
                }
            } else if (requestCode == PERMISSION_REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
               if (mBrightnessPromise != null) {
                    mBrightnessPromise.reject(TAG, Messages.PERMISSION_DENIED);
               }  
            } 
        }
    };

    public Brightness(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(permissionsListener);   
    }

    @Override
    public String getName() {
        return "Brightness";
    }
     
    @ReactMethod
    public void index(Promise promise) {
        try {
            WritableMap map = new WritableNativeMap();
            int br = Settings.System.getInt(getReactApplicationContext().getContentResolver(), 
                                   Settings.System.SCREEN_BRIGHTNESS);            
            map.putInt("brightness", br);
            promise.resolve(map);
        } catch(Exception ex) {
            promise.reject(TAG, ex.getMessage());
        }
    }

    @ReactMethod
    public void indexTo(int value, Promise promise) {
        try {
            if (value > 0 && value <= 255) {
                mBrightness = value;
                Activity activity = getCurrentActivity();

                assert activity != null;

                if (Settings.System.canWrite(getReactApplicationContext())) {
                    setBrightness();
                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            } else {
                promise.reject(TAG, Messages.BRIGHTNESS_INVALID_INDEX);
            }
        } catch (Exception ex) {
            promise.reject(TAG, ex.getMessage());
        }
    }

    private void setBrightness() {
        Settings.System.putInt( getReactApplicationContext().getContentResolver(), 
                                Settings.System.SCREEN_BRIGHTNESS,
                                mBrightness
                              );
    }
}
