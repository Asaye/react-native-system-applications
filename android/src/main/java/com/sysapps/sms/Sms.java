package com.sysapps.sms;

import com.sysapps.utils.Messages;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;

import android.telephony.SmsManager; 
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.Context;
import android.os.Process;

import java.util.ArrayList;

public class Sms extends ReactContextBaseJavaModule 
            implements PermissionListener {

    private static final String TAG = "SMS_ERROR";
    private static final int PERMISSIONS_REQUEST_CODE = 10060;    

    private static final String[] PERMISSIONS = 
                        new String[] {   
                            Manifest.permission.SEND_SMS, 
                        };

    private boolean mIsPermissionGranted = false;

    private SmsManager mSmsManager = SmsManager.getDefault();
    private String mNumber;
    private ArrayList<String> mMessage;
    private Promise mSmsPromise;

    public Sms(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "Sms";
    }  

    @Override
    public boolean onRequestPermissionsResult(
        int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && 
            grantResults.length == PERMISSIONS.length) {
            mIsPermissionGranted = true;
            sendSms();
        } else {
            mSmsPromise.reject(TAG, Messages.PERMISSION_DENIED);
        }
        return true;
    }
     
    @ReactMethod
    public void send(String num, String message, Promise promise) {        
        mNumber = num;
        mSmsPromise = promise;
        mMessage = mSmsManager.divideMessage(message);        
        
        checkPermission();
    }

    private void checkPermission() {        
        int pid = Process.myPid();
        int uid = Process.myPid();
        int status_ok = PackageManager.PERMISSION_GRANTED;
        Activity activity = getCurrentActivity();

        assert activity == null;
        Context context = getReactApplicationContext().getBaseContext();

        mIsPermissionGranted = true;
        for (String permission : PERMISSIONS) {
            if (context.checkPermission(permission, pid, uid) != status_ok) {
                mIsPermissionGranted = false;
                ((PermissionAwareActivity) activity)
                        .requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE, this);
                break;
            }
        } 

        if (mIsPermissionGranted) {
           sendSms();
        }       
    }

    private void sendSms() {
        try {       
            mSmsManager.sendMultipartTextMessage(mNumber, null, mMessage, null, null);
        } catch(Exception ex) {
            mSmsPromise.reject(TAG, ex.getMessage());
        }
    }
}
