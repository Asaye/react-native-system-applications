package com.sysapps.image;

import com.sysapps.utils.Messages;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;
import com.facebook.react.bridge.ReadableMap;

import android.content.Context;
import android.Manifest;
import android.content.pm.PackageManager;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Process;

import java.util.HashMap;

public class ImageCapturer extends ReactContextBaseJavaModule 
            implements PermissionListener {

    private static final String TAG = "IMAGE_CAPTURE_ERROR";
    private static final int PERMISSIONS_REQUEST_CODE = 10120;    

    private static final String[] PERMISSIONS = 
                        new String[] {   
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, 
                            Manifest.permission.CAMERA 
                        };
    private boolean mIsPermissionGranted = false;
    private Promise mImageCapturePromise;
    private HashMap mRequestData;
    private FragmentManager mFragmentManager;
    private ImageFragment mImageFragment;

    public ImageCapturer(ReactApplicationContext reactContext) {
        super(reactContext); 
    }

    @Override
    public String getName() {
        return "Image";
    }
    
    @Override
    public boolean onRequestPermissionsResult(
        int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && 
            grantResults.length == PERMISSIONS.length) {
            mIsPermissionGranted = true;
            requestCapture();
        } else {
            mImageCapturePromise.reject(TAG, Messages.PERMISSION_DENIED);
        }
        return true;
    }   

    @ReactMethod
    public void prepare(ReadableMap map, Promise promise) {
        mRequestData = map.toHashMap();
        mImageCapturePromise = promise;
        checkPermission();
    }

    @ReactMethod
    public void capture(Promise promise) {

        Activity activity = getCurrentActivity();

        assert activity == null;
        activity.runOnUiThread(new Runnable() {
            public void run() {                
                if (mImageFragment != null) {
                    mImageFragment.takePicture();
                }                
            }
        });
    }    

    @ReactMethod
    public void exitCapture() {

        Activity activity = getCurrentActivity();

        assert activity == null;

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (mImageFragment != null) {
                    mImageFragment.exitImageCapture();
                    mImageFragment = null;
                }
            }
        });
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
            requestCapture();
        }        
    }

    private void requestCapture() {
        try {
            Activity activity = getCurrentActivity();

            assert activity == null;

            if (mRequestData == null || mRequestData.get("outputFolder") == null) {
                mImageCapturePromise.reject(TAG, Messages.NO_OUTPUT_FOLDER);
                return;
            }                              
                                                  
            mFragmentManager = activity.getFragmentManager();
            mImageFragment = ImageFragment.newInstance();                  
            FragmentTransaction transaction = mFragmentManager.beginTransaction();
            Bundle bundle = new Bundle(); 
            bundle.putSerializable("recorderData", mRequestData);  
            mImageFragment.setArguments(bundle);       
            transaction.replace(android.R.id.content, mImageFragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
            
        } catch(Exception ex)  {
            if (mImageCapturePromise != null) {
                mImageCapturePromise.reject(TAG, ex.getMessage());
            }            
        }
    }        
}
