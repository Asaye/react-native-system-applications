package com.sysapps.files;

import com.sysapps.utils.Messages;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.LifecycleEventListener;
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
import android.os.Handler;
import android.os.HandlerThread;

import java.lang.Exception;
import java.net.URL;
import java.net.URLDecoder;
import java.io.File;

public class Files extends ReactContextBaseJavaModule 
            implements PermissionListener {

    private static final String TAG = "FILE_OPEN_ERROR";
    private static final int FILE_PICK_REQUEST = 10050;
    private static final int FILE_OPEN_REQUEST = 10051;
    private static final int FILE_TRANSFER_REQUEST = 10052;
    private static final int FILE_UPLOAD_REQUEST = 10053;
    private static final int PERMISSIONS_REQUEST_OPEN = 10054;
    private static final int PERMISSIONS_REQUEST_PICK = 10055;
    private static final int PERMISSIONS_REQUEST_DOWNLOAD = 10056;
    private static final int PERMISSIONS_REQUEST_UPLOAD = 10057;
    

    private static final String[] PERMISSIONS = 
                        new String[] {   
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        };
    
    private Promise mFilePickPromise;
    private Promise mFileOpenPromise;
    private Promise mFileTransferPromise;
    private Promise mFileUploadPromise;
    private Uri mFileUri;
    private String mUrl;
    private String mSource;
    private String mDestination;

    private final ActivityEventListener filesEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {

            if (mFilePickPromise != null && requestCode == FILE_PICK_REQUEST) {
                if (resultCode == Activity.RESULT_OK) {                    
                    Uri uri = intent.getData();

                    if (uri == null) {
                        mFilePickPromise.reject(TAG, "No file data found");
                    } else {
                        String path = URLDecoder.decode(uri.toString());
                        mFilePickPromise.resolve(path);
                    }
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    mFilePickPromise.reject(TAG, Messages.PICKER_CANCELLED);
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
                    mFileOpenPromise.reject(TAG, Messages.PICKER_CANCELLED);
                }
                mFileOpenPromise = null;
            } else if (requestCode == FILE_UPLOAD_REQUEST) {

                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = intent.getData();

                    if (uri != null) {
                        String path = uri.getPath();
                        File file = new File(path);

                        if (!file.isFile() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            try {
                                Context context = getReactApplicationContext();
                                String packageName = context.getPackageName();
                                Uri fpUri = FileProvider.getUriForFile(getCurrentActivity(), packageName, file);
                                context.grantUriPermission(packageName, fpUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                path = URLDecoder.decode(fpUri.toString()); 
                                file = new File(path);
                                if (!file.isFile()) {
                                    mFileUploadPromise.reject(TAG, Messages.FILE_INACCESSIBLE);
                                }
                            } catch (Exception ex) {
                                mFileUploadPromise.reject(TAG, ex.getMessage());
                            }
                        }                        
                        HandlerThread thread = new HandlerThread("FileUpload");
                        thread.start();
                        Handler handler = new Handler(thread.getLooper());     
                        handler.post(new Uploader(file, mDestination, mFileUploadPromise));
                    }                  
                } else if (mFileUploadPromise != null && resultCode == Activity.RESULT_CANCELED) {
                    mFileUploadPromise.reject(TAG, "User has cancelled the file picker");
                }
                mFileUploadPromise = null;
            } else if (mFileTransferPromise != null && requestCode == FILE_TRANSFER_REQUEST) {

                if (resultCode == Activity.RESULT_OK) {
                    downloadFile();                   
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    mFileTransferPromise.reject(TAG, Messages.PERMISSION_DENIED);
                }
                mFileTransferPromise = null;
            } 
        }
    };

    private LifecycleEventListener lifeEventListener = new LifecycleEventListener() {
        public void onHostResume() { 
        }

        public void onHostPause() { 
        }

        public void onHostDestroy() { 
            if (mFileUri == null) return;
            Context context = getReactApplicationContext();
            if (context == null) return;
            String packageName = context.getPackageName();
            context.grantUriPermission(packageName, mFileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION 
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    };
    
    public Files(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(filesEventListener);
        reactContext.addLifecycleEventListener(lifeEventListener);
    }

    @Override
    public String getName() {
        return "Files";
    }

    @Override
    public boolean onRequestPermissionsResult(
        int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length == PERMISSIONS.length) {
            if (requestCode == PERMISSIONS_REQUEST_OPEN) {
                getUri();
            } else if (requestCode == PERMISSIONS_REQUEST_PICK) {
                openDialog(FILE_OPEN_REQUEST);
            } else if (requestCode == PERMISSIONS_REQUEST_DOWNLOAD) {
                downloadFile();
            } else if (requestCode == PERMISSIONS_REQUEST_UPLOAD) {
                openDialog(FILE_UPLOAD_REQUEST);
            }            
        } else {
            mFileOpenPromise.reject(TAG, Messages.PERMISSION_DENIED);
        }
        return true;
    } 

    @ReactMethod
    public void open(String url, Promise promise) {
        mUrl = url;
        mFileOpenPromise = promise;
        if (checkPermission()) {
            getUri();
        } else {
            requestPermissions(PERMISSIONS_REQUEST_OPEN); 
        }
    }

    @ReactMethod
    public void pick(Promise promise) {  
        mFileOpenPromise = promise;
        if (checkPermission()) {
            openDialog(FILE_OPEN_REQUEST);
        } else {
            requestPermissions(PERMISSIONS_REQUEST_PICK); 
        }            
    }

    @ReactMethod
    public void getPath(Promise promise) {
        mFilePickPromise = promise;
        openDialog(FILE_PICK_REQUEST);        
    }

    @ReactMethod
    public void download(String src, String dest, Promise promise) { 
        mSource = src;
        mDestination = dest; 
        mFileTransferPromise = promise;
        if (checkPermission()) {
            downloadFile();
        } else {
            requestPermissions(PERMISSIONS_REQUEST_DOWNLOAD); 
        }
    } 

    @ReactMethod
    public void upload(String dest, Promise promise) {
        mDestination = dest; 
        mFileUploadPromise = promise;
        if (checkPermission()) {
            openDialog(FILE_UPLOAD_REQUEST);
        } else {
            requestPermissions(PERMISSIONS_REQUEST_UPLOAD); 
        }        
    }   

    private boolean checkPermission() {        
        int pid = Process.myPid();
        int uid = Process.myUid();
        int status_ok = PackageManager.PERMISSION_GRANTED;        
        Context context = getReactApplicationContext().getBaseContext();

        for (String permission : PERMISSIONS) {
            if (context.checkPermission(permission, pid, uid) != status_ok) {
                return false;
            }
        }

        return true;   
    }

    private void requestPermissions(int requestCode) {
        Activity activity = getCurrentActivity();
        if (activity == null) return;
        ((PermissionAwareActivity) activity)
                        .requestPermissions(PERMISSIONS,requestCode, this); 
    }

    private void openDialog(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        getCurrentActivity().startActivityForResult(intent, requestCode);
    }

    private void getUri() {
        try {  

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                openProvidedFile();                
            } else { 
                openFile();
            }
            
        } catch (Exception ex) {
            if (mFileOpenPromise != null) {
                mFileOpenPromise.reject(TAG, ex.getMessage());
            }
        }
    }

    private void downloadFile() {
        HandlerThread thread = new HandlerThread("FileDownload");
        thread.start();
        Handler handler = new Handler(thread.getLooper());                        
        handler.post(new Downloader(mSource, mDestination, mFileTransferPromise));
    }
    
    private void openProvidedFile() {
        Activity activity = getCurrentActivity();
        if (activity == null) return;

        PackageManager pm = activity.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(mUrl);
        Context context = getReactApplicationContext();
        String packageName = context.getPackageName();
        mFileUri = FileProvider.getUriForFile(activity, packageName, file); 
        String mimeType = null;

        context.grantUriPermission(packageName, mFileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | 
                                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | 
                         Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        mimeType = context.getContentResolver().getType(mFileUri);
        intent.setDataAndType(mFileUri, mimeType);
        
        if (intent.resolveActivity(pm) != null) {
            activity.startActivity(intent);
        }
    }

    private void openFile() {

        Activity activity = getCurrentActivity();

        if (activity == null) return;

        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(mUrl);
        Uri uri = Uri.fromFile(file);
        String url = uri.toString();
                
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
