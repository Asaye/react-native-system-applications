package com.sysapps.bluetooth;

import com.sysapps.utils.Messages;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;

import android.bluetooth.BluetoothAdapter;
import android.app.Activity;
import android.Manifest;
import android.content.pm.PackageManager;
import android.content.Context;
import android.os.Process;

public class Bluetooth extends ReactContextBaseJavaModule 
            implements PermissionListener {

    private static final String TAG = "BLUETOOTH_ERROR";
    private static final int PERMISSIONS_REQUEST_CODE = 10010;

    private static final String[] PERMISSIONS = 
                        new String[] {   
                            Manifest.permission.BLUETOOTH
                        };

    private boolean mIsPermissionGranted = false;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private Promise mBluetoothPromise;
    private String mBluetoothTarget;

    public Bluetooth(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "Bluetooth";
    }

    @Override
    public boolean onRequestPermissionsResult(
        int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && 
            grantResults.length == PERMISSIONS.length) {
            mIsPermissionGranted = true;
            changeBluetoothStatus();  
        } else {
            mBluetoothPromise.reject(TAG, Messages.PERMISSION_DENIED);
        }
        return true;
    }
     
    @ReactMethod
    public void enable(Promise promise) {
        mBluetoothTarget = "enable";
        mBluetoothPromise = promise;
        checkPermission();
    }

    @ReactMethod
    public void disable(Promise promise) {
        mBluetoothTarget = "disable";
        mBluetoothPromise = promise;
        checkPermission();
    }

    @ReactMethod
    public void isEnabled(Promise promise) {
        try {
            boolean enabled = (boolean) mBluetoothAdapter.isEnabled();
            promise.resolve(enabled);
        } catch (Exception ex) {
            promise.reject(TAG, ex.getMessage());
        }
    }

    private void checkPermission() {        
        int pid = Process.myPid();
        int uid = Process.myPid();
        int status_ok = PackageManager.PERMISSION_GRANTED;
        Activity activity = getCurrentActivity();
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
            changeBluetoothStatus();
        }       
    }

    private void changeBluetoothStatus() {
        Activity activity = getCurrentActivity();
        try {
            assert activity == null && mBluetoothAdapter != null;

            if (mBluetoothTarget != null && mBluetoothTarget.equals("enable")) {
                mBluetoothAdapter.enable();
            } else if (mBluetoothTarget != null && mBluetoothTarget.equals("disable")) {
                mBluetoothAdapter.disable();
            }            
        } catch (Exception ex) {
            mBluetoothPromise.reject(TAG, ex.getMessage());
        }
    }    
}
