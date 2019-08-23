package com.sysapps.calls;

import com.sysapps.utils.Messages;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;

import android.content.Intent;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.Context;
import android.Manifest;
import android.database.Cursor;
import android.provider.CallLog.Calls;
import android.net.Uri;
import android.os.Process;

public class PhoneCalls extends ReactContextBaseJavaModule 
            implements PermissionListener {

    private static final String TAG = "CALLS_ERROR";
    private static final int PERMISSION_CALL_PHONE = 10030;
    private static final int PERMISSION_CALL_LOG = 10031;

    private static final String[] PERMISSIONS_PHONE = 
                        new String[] {   
                            Manifest.permission.CALL_PHONE
                        };

    private static final String[] PERMISSIONS_LOG = 
                        new String[] {   
                            Manifest.permission.READ_CALL_LOG
                        };

    private boolean mIsPermissionGranted = false;

    private Promise mCallsPromise;
    private String mNumber;   

    public PhoneCalls(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "Calls";
    }

    @Override
    public boolean onRequestPermissionsResult(
        int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_CALL_PHONE && 
            grantResults.length == PERMISSIONS_PHONE.length) {
            mIsPermissionGranted = true;
            callNumber();
        } else if (requestCode == PERMISSION_CALL_LOG && 
            grantResults.length == PERMISSIONS_LOG.length) {
            mIsPermissionGranted = true;
            sendCallLog(); 
        } else {
            mCallsPromise.reject(TAG, Messages.PERMISSION_DENIED);
        }
        return true;
    } 

    @ReactMethod
    public void call(String num, Promise promise) {
        Activity activity = getCurrentActivity();

        assert activity != null && num != null;
        mNumber = num;
        mCallsPromise = promise;
        checkPermission(PERMISSIONS_PHONE, PERMISSION_CALL_PHONE, "phone");
    }  

    @ReactMethod
    public void getLog(Promise promise) {
        Activity activity = getCurrentActivity();

        assert activity != null;
        mCallsPromise = promise;
        checkPermission(PERMISSIONS_LOG, PERMISSION_CALL_LOG, "log");
    }

    private void checkPermission(String[] permissions, int requestCode, String type) {        
        int pid = Process.myPid();
        int uid = Process.myPid();
        int status_ok = PackageManager.PERMISSION_GRANTED;
        Activity activity = getCurrentActivity();
        Context context = getReactApplicationContext().getBaseContext();

        mIsPermissionGranted = true;
        for (String permission : permissions) {
            if (context.checkPermission(permission, pid, uid) != status_ok) {
                mIsPermissionGranted = false;
                ((PermissionAwareActivity) activity)
                        .requestPermissions(permissions, requestCode, this);
                break;
            }
        }

        if (mIsPermissionGranted && type.equals("phone")) {
            callNumber();
        } else if (mIsPermissionGranted && type.equals("log")) {
            sendCallLog();
        }        
    }

    private void callNumber() {
        Activity activity = getCurrentActivity();

        assert activity != null;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + mNumber));
        activity.startActivity(intent);
    }

    private void sendCallLog() {
        
        WritableMap map = null;
        WritableArray array = new WritableNativeArray();

        Cursor cursor = getReactApplicationContext().getContentResolver()
              .query(Calls.CONTENT_URI, null, null, null, null);

        int number = cursor.getColumnIndex(Calls.NUMBER);
        int type = cursor.getColumnIndex(Calls.TYPE);
        int date = cursor.getColumnIndex(Calls.DATE);
        int duration = cursor.getColumnIndex(Calls.DURATION);
        if (cursor.moveToFirst()) {
            do {
                map = new WritableNativeMap();  
                int callType_int = Integer.parseInt(cursor.getString(type));
                String callType = "";
                if (Calls.OUTGOING_TYPE == callType_int) callType = "OUTGOING";
                else if (Calls.INCOMING_TYPE == callType_int) callType = "INCOMING";
                else if (Calls.MISSED_TYPE == callType_int) callType = "MISSED";          
                map.putString("Number", cursor.getString(number));
                map.putString("Type", callType);
                map.putString("Date", cursor.getString(date));
                map.putInt("Duration", cursor.getInt(duration));
                array.pushMap(map);
            } while (cursor.moveToNext());
            mCallsPromise.resolve(array);
        } else {
            mCallsPromise.reject(TAG, "No call log could be retrieved.");
        }

        if (cursor != null) {
         cursor.close();
        }
    }
}
