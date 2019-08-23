package com.sysapps.files;

import com.sysapps.utils.Messages;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.modules.core.PermissionAwareActivity;
import com.facebook.react.modules.core.PermissionListener;

import android.net.Uri;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.Manifest;
import android.os.Bundle;
import android.os.Build;
import android.os.Process;

import java.lang.Exception;
import java.io.File;

public class Files extends ReactContextBaseJavaModule 
            implements PermissionListener {

    private static final String TAG = "FILE_OPEN_ERROR";
    private static final int PERMISSIONS_REQUEST_CODE = 10050;
    private static final int FILE_PICK_REQUEST = 10051;
    private static final int FILE_OPEN_REQUEST = 10052;

    private static final String[] PERMISSIONS = 
                        new String[] {   
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        };
    private boolean mIsPermissionGranted = false;    

    private Promise mFilePickPromise;
    private Promise mFileOpenPromise;
    private String mUrl;

    private final ActivityEventListener filesEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {

            if (mFilePickPromise != null && requestCode == FILE_PICK_REQUEST) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = intent.getData();
                    if (uri == null) {
                        mFilePickPromise.reject(TAG, "No file data found");
                    } else {
                        mFilePickPromise.resolve(uri.toString());
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    mFilePickPromise.reject(TAG, "User has cancelled the file picker");
                }
                mFilePickPromise = null;
            } else if (mFileOpenPromise != null && requestCode == FILE_OPEN_REQUEST) {
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = intent.getData();
                    if (uri != null) {
                        mUrl = uri.getPath();
                        getUri();
                    }                    
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    mFileOpenPromise.reject(TAG, "User has cancelled the file picker");
                }
                mFileOpenPromise = null;
            } 
        }
    };
    
    public Files(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(filesEventListener);
    }

    @Override
    public String getName() {
        return "Files";
    }

    @Override
    public boolean onRequestPermissionsResult(
        int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && 
            grantResults.length == PERMISSIONS.length) {
            mIsPermissionGranted = true;
            getUri();
        } else {
            mFileOpenPromise.reject(TAG, Messages.PERMISSION_DENIED);
        }
        return true;
    } 

    @ReactMethod
    public void open(String url, Promise promise) {
        mUrl = url;
        mFileOpenPromise = promise;
        checkPermission("open");
    }

    @ReactMethod
    public void pick(Promise promise) {  
        mFileOpenPromise = promise;
        checkPermission("pick");
    }

    @ReactMethod
    public void getPath(Promise promise) {
        
        Activity activity = getCurrentActivity();

        assert activity != null;
        mFilePickPromise = promise;
        openDialog(FILE_PICK_REQUEST);        
    }    

    private void checkPermission(String type) {        
        int pid = Process.myPid();
        int uid = Process.myPid();
        int status_ok = PackageManager.PERMISSION_GRANTED;
        Activity activity = getCurrentActivity();
        
        assert activity != null;
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

        if (mIsPermissionGranted && type.equals("open")) {
            getUri();
        } else if (mIsPermissionGranted && type.equals("pick")) {            
            openDialog(FILE_OPEN_REQUEST);
        }       
    }

    private void openDialog(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        getCurrentActivity().startActivityForResult(intent, requestCode);
    }

    private void getUri() {
        Activity activity = getCurrentActivity();

        assert activity != null;

        try {            

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File file = new File(mUrl);
                Context context = getReactApplicationContext();
                Uri uri = FileProvider.getUriForFile(activity, context.getPackageName(), file); 
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                openFile(uri, intent);
            } else {                
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File file = new File(mUrl);
                Uri uri = Uri.fromFile(file);
                openFile(uri, intent);
            }
            
        } catch (Exception ex) {
            if (mFileOpenPromise != null) {
                mFileOpenPromise.reject(TAG, ex.getMessage());
            }
        }
    }

    private void openFile(Uri uri, Intent intent) {

        String url = uri.toString();
        Activity activity = getCurrentActivity();

        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            intent.setDataAndType(uri, "application/msword");
        } else if(url.toString().contains(".pdf")) {
            intent.setDataAndType(uri, "application/pdf");
        } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
            intent.setDataAndType(uri, "application/x-wav");
        } else if(url.toString().contains(".rtf")) {
            intent.setDataAndType(uri, "application/rtf");
        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.toString().contains(".gif")) {
            intent.setDataAndType(uri, "image/gif");
        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || 
                  url.toString().contains(".png")) {
            intent.setDataAndType(uri, "image/jpeg");
        } else if(url.toString().contains(".txt")) {
            intent.setDataAndType(uri, "text/plain");
        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || 
            url.toString().contains(".mpeg") || url.toString().contains(".mpe") || 
            url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            intent.setDataAndType(uri, "video/*");
        } else {
            intent.setDataAndType(uri, "*/*");
        }

        PackageManager pm = activity.getPackageManager();
        if (intent.resolveActivity(pm) != null) {
            activity.startActivity(intent);
        }
    }
}
