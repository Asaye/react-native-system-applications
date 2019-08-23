package com.sysapps.wifi;

import com.sysapps.utils.Messages;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Process;

public class Wifi extends ReactContextBaseJavaModule 
            implements PermissionListener {

    private static final String TAG = "WIFI_ERROR";
    private static final int PERMISSION_ACCESS_WIFI = 10110;
    private static final int PERMISSION_CHANGE_WIFI = 10111;

    private static final String[] PERMISSIONS_ACCESS = 
                        new String[] {   
                            Manifest.permission.ACCESS_WIFI_STATE
                        };

    private static final String[] PERMISSIONS_CHANGE = 
                        new String[] {   
                            Manifest.permission.CHANGE_WIFI_STATE
                        };

    private Promise mWifiPromise;
    private boolean mWifiStateTarget;

    private WifiManager mWifiManager 
            = (WifiManager) getReactApplicationContext().getSystemService(Context.WIFI_SERVICE);
    
    public Wifi(ReactApplicationContext reactContext) {
        super(reactContext);        
    }

    @Override
    public String getName() {
        return "Wifi";
    }

    @Override
    public boolean onRequestPermissionsResult(
        int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == PERMISSION_ACCESS_WIFI && 
            grantResults.length == PERMISSIONS_ACCESS.length) {
            accessWifiState();
        } else if (requestCode == PERMISSION_CHANGE_WIFI && 
            grantResults.length == PERMISSIONS_CHANGE.length) {            
            changeWifiState();
        } else {
            mWifiPromise.reject(TAG, Messages.PERMISSION_DENIED);
        }

        return true;
    } 

    @ReactMethod
    public void enable(Promise promise) {
        mWifiStateTarget = true;
        mWifiPromise = promise;
        checkPermission(PERMISSIONS_CHANGE, PERMISSION_CHANGE_WIFI, "change");        
    }

    @ReactMethod
    public void disable(Promise promise) {        
        mWifiStateTarget = false;
        mWifiPromise = promise;
        checkPermission(PERMISSIONS_CHANGE, PERMISSION_CHANGE_WIFI, "change");
    }       

    @ReactMethod
    public void isEnabled(Promise promise) {
        mWifiPromise = promise;
        checkPermission(PERMISSIONS_ACCESS, PERMISSION_ACCESS_WIFI, "access");
    }

    private void accessWifiState() {
        assert mWifiManager != null;

        try {         
            boolean enabled = (boolean) mWifiManager.isWifiEnabled();
            mWifiPromise.resolve(enabled);            
        } catch (Exception ex) {
            mWifiPromise.reject(TAG, ex.getMessage());
        }
    }

    private void checkPermission(String[] permissions, int requestCode, String type) {        
        int pid = Process.myPid();
        int uid = Process.myPid();
        int status_ok = PackageManager.PERMISSION_GRANTED;

        Activity activity = getCurrentActivity();

        assert activity != null;
        Context context = getReactApplicationContext().getBaseContext();

        boolean isPermissionGranted = true;
        for (String permission : permissions) {
            if (context.checkPermission(permission, pid, uid) != status_ok) {
                isPermissionGranted = false;
                ((PermissionAwareActivity) activity)
                        .requestPermissions(permissions, requestCode, this);
                break;
            }
        } 

        if (isPermissionGranted && type.equals("change")) {
            changeWifiState();
        } else if (isPermissionGranted && type.equals("access")) {
            accessWifiState();
        }    
    }

    private void changeWifiState() {
        assert mWifiManager != null;
       
        try {         
            mWifiManager.setWifiEnabled(mWifiStateTarget);            
        } catch (Exception ex) {
            mWifiPromise.reject(TAG, ex.getMessage());
        }
    }    
}
